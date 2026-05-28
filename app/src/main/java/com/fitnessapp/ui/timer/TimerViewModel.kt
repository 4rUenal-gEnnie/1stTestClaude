package com.fitnessapp.ui.timer

import android.app.Application
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fitnessapp.service.TimerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TimerMode { COUNTDOWN, INTERVAL }
enum class TimerStatus { IDLE, RUNNING, PAUSED, FINISHED }

data class TimerUiState(
    val mode: TimerMode = TimerMode.COUNTDOWN,
    val status: TimerStatus = TimerStatus.IDLE,
    val remainingSeconds: Int = 60,
    val totalSeconds: Int = 60,
    // Interval mode
    val workSeconds: Int = 30,
    val restSeconds: Int = 15,
    val totalRounds: Int = 8,
    val currentRound: Int = 1,
    val isWorkPhase: Boolean = true
) {
    val progressFraction: Float
        get() = if (totalSeconds > 0) remainingSeconds.toFloat() / totalSeconds else 0f
}

@HiltViewModel
class TimerViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val context = application.applicationContext

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState

    // Countdown settings
    private val _countdownInput = MutableStateFlow(60)
    val countdownInput: StateFlow<Int> = _countdownInput

    private var timerJob: Job? = null

    fun setMode(mode: TimerMode) {
        if (_uiState.value.status == TimerStatus.IDLE || _uiState.value.status == TimerStatus.FINISHED) {
            _uiState.value = _uiState.value.copy(mode = mode, status = TimerStatus.IDLE)
        }
    }

    fun setCountdownSeconds(seconds: Int) {
        _countdownInput.value = seconds
        if (_uiState.value.status == TimerStatus.IDLE) {
            _uiState.value = _uiState.value.copy(remainingSeconds = seconds, totalSeconds = seconds)
        }
    }

    fun setWorkSeconds(seconds: Int) {
        _uiState.value = _uiState.value.copy(workSeconds = seconds)
    }

    fun setRestSeconds(seconds: Int) {
        _uiState.value = _uiState.value.copy(restSeconds = seconds)
    }

    fun setTotalRounds(rounds: Int) {
        _uiState.value = _uiState.value.copy(totalRounds = rounds)
    }

    fun start() {
        val state = _uiState.value
        when (state.mode) {
            TimerMode.COUNTDOWN -> {
                val seconds = if (state.status == TimerStatus.IDLE || state.status == TimerStatus.FINISHED)
                    _countdownInput.value else state.remainingSeconds
                _uiState.value = state.copy(
                    status = TimerStatus.RUNNING,
                    remainingSeconds = seconds,
                    totalSeconds = _countdownInput.value
                )
                startTimerService()
                startCountdown()
            }
            TimerMode.INTERVAL -> {
                val workSec = state.workSeconds
                _uiState.value = state.copy(
                    status = TimerStatus.RUNNING,
                    remainingSeconds = workSec,
                    totalSeconds = workSec,
                    currentRound = 1,
                    isWorkPhase = true
                )
                startTimerService()
                startInterval()
            }
        }
    }

    fun pause() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(status = TimerStatus.PAUSED)
    }

    fun resume() {
        _uiState.value = _uiState.value.copy(status = TimerStatus.RUNNING)
        when (_uiState.value.mode) {
            TimerMode.COUNTDOWN -> startCountdown()
            TimerMode.INTERVAL -> startInterval()
        }
    }

    fun reset() {
        timerJob?.cancel()
        stopTimerService()
        val state = _uiState.value
        _uiState.value = state.copy(
            status = TimerStatus.IDLE,
            remainingSeconds = _countdownInput.value,
            totalSeconds = _countdownInput.value,
            currentRound = 1,
            isWorkPhase = true
        )
    }

    private fun startCountdown() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.remainingSeconds > 0 && _uiState.value.status == TimerStatus.RUNNING) {
                delay(1_000)
                val current = _uiState.value.remainingSeconds - 1
                _uiState.value = _uiState.value.copy(remainingSeconds = current)
            }
            if (_uiState.value.remainingSeconds == 0 && _uiState.value.status == TimerStatus.RUNNING) {
                _uiState.value = _uiState.value.copy(status = TimerStatus.FINISHED)
                playAlert()
                stopTimerService()
            }
        }
    }

    private fun startInterval() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var state = _uiState.value
            while (state.status == TimerStatus.RUNNING) {
                delay(1_000)
                state = _uiState.value
                if (state.status != TimerStatus.RUNNING) break
                val newRemaining = state.remainingSeconds - 1
                if (newRemaining <= 0) {
                    playAlert()
                    val nextIsWork = !state.isWorkPhase
                    val nextRound = if (nextIsWork) state.currentRound + 1 else state.currentRound
                    if (!nextIsWork && nextRound > state.totalRounds) {
                        _uiState.value = state.copy(status = TimerStatus.FINISHED, remainingSeconds = 0)
                        stopTimerService()
                        break
                    }
                    val nextDuration = if (nextIsWork) state.workSeconds else state.restSeconds
                    _uiState.value = state.copy(
                        remainingSeconds = nextDuration,
                        totalSeconds = nextDuration,
                        isWorkPhase = nextIsWork,
                        currentRound = if (nextIsWork) nextRound else state.currentRound
                    )
                    state = _uiState.value
                } else {
                    _uiState.value = state.copy(remainingSeconds = newRemaining)
                    state = _uiState.value
                }
            }
        }
    }

    private fun playAlert() {
        try {
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val ringtone = RingtoneManager.getRingtone(context, alarmUri)
            ringtone.play()
        } catch (_: Exception) {}

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(VibratorManager::class.java)
                vm?.defaultVibrator?.vibrate(
                    VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Vibrator::class.java)
                vibrator?.vibrate(
                    VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            }
        } catch (_: Exception) {}
    }

    private fun startTimerService() {
        try {
            val intent = Intent(context, TimerService::class.java).apply {
                putExtra(TimerService.EXTRA_TOTAL_SECONDS, _uiState.value.totalSeconds)
            }
            context.startForegroundService(intent)
        } catch (_: Exception) {}
    }

    private fun stopTimerService() {
        try {
            context.stopService(Intent(context, TimerService::class.java))
        } catch (_: Exception) {}
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        stopTimerService()
    }
}
