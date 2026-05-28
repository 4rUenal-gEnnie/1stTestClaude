package com.fitnessapp.ui.timer

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fitnessapp.R

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val countdownInput by viewModel.countdownInput.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.timer_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(vertical = 8.dp)
        )

        // Mode selector
        if (uiState.status == TimerStatus.IDLE || uiState.status == TimerStatus.FINISHED) {
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = uiState.mode == TimerMode.COUNTDOWN,
                    onClick = { viewModel.setMode(TimerMode.COUNTDOWN) },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                ) {
                    Text(stringResource(R.string.countdown))
                }
                SegmentedButton(
                    selected = uiState.mode == TimerMode.INTERVAL,
                    onClick = { viewModel.setMode(TimerMode.INTERVAL) },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                ) {
                    Text(stringResource(R.string.interval))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.mode == TimerMode.COUNTDOWN) {
                CountdownSettings(
                    seconds = countdownInput,
                    onSecondsChange = viewModel::setCountdownSeconds
                )
            } else {
                IntervalSettings(
                    workSeconds = uiState.workSeconds,
                    restSeconds = uiState.restSeconds,
                    totalRounds = uiState.totalRounds,
                    onWorkChange = viewModel::setWorkSeconds,
                    onRestChange = viewModel::setRestSeconds,
                    onRoundsChange = viewModel::setTotalRounds
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Timer display
        Spacer(modifier = Modifier.weight(1f))
        TimerDisplay(uiState = uiState)
        Spacer(modifier = Modifier.weight(1f))

        // Controls
        TimerControls(
            status = uiState.status,
            onStart = viewModel::start,
            onPause = viewModel::pause,
            onResume = viewModel::resume,
            onReset = viewModel::reset
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun CountdownSettings(
    seconds: Int,
    onSecondsChange: (Int) -> Unit
) {
    var inputText by remember(seconds) { mutableStateOf(seconds.toString()) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.duration_seconds),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { v ->
                        inputText = v
                        v.toIntOrNull()?.let { if (it > 0) onSecondsChange(it) }
                    },
                    label = { Text(stringResource(R.string.seconds)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    listOf(30, 60, 90, 120, 180, 300).chunked(3).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            row.forEach { preset ->
                                AssistChip(
                                    onClick = {
                                        onSecondsChange(preset)
                                        inputText = preset.toString()
                                    },
                                    label = { Text("${preset}s", style = MaterialTheme.typography.labelSmall) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IntervalSettings(
    workSeconds: Int,
    restSeconds: Int,
    totalRounds: Int,
    onWorkChange: (Int) -> Unit,
    onRestChange: (Int) -> Unit,
    onRoundsChange: (Int) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.interval_settings),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NumberField(
                    label = stringResource(R.string.work_s),
                    value = workSeconds,
                    onValueChange = onWorkChange,
                    modifier = Modifier.weight(1f)
                )
                NumberField(
                    label = stringResource(R.string.rest_s),
                    value = restSeconds,
                    onValueChange = onRestChange,
                    modifier = Modifier.weight(1f)
                )
                NumberField(
                    label = stringResource(R.string.rounds),
                    value = totalRounds,
                    onValueChange = onRoundsChange,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun NumberField(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember(value) { mutableStateOf(value.toString()) }
    OutlinedTextField(
        value = text,
        onValueChange = { v ->
            text = v
            v.toIntOrNull()?.let { if (it > 0) onValueChange(it) }
        },
        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
        singleLine = true,
        modifier = modifier
    )
}

@Composable
private fun TimerDisplay(uiState: TimerUiState) {
    val progress by animateFloatAsState(
        targetValue = uiState.progressFraction,
        animationSpec = tween(durationMillis = 300),
        label = "timerProgress"
    )

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(220.dp)) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 12.dp,
            color = when {
                uiState.status == TimerStatus.FINISHED -> MaterialTheme.colorScheme.error
                uiState.mode == TimerMode.INTERVAL && !uiState.isWorkPhase ->
                    MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.primary
            },
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = formatTime(uiState.remainingSeconds),
                style = MaterialTheme.typography.displayMedium.copy(fontSize = 48.sp),
                fontWeight = FontWeight.Bold
            )
            if (uiState.mode == TimerMode.INTERVAL && uiState.status != TimerStatus.IDLE) {
                Text(
                    text = if (uiState.isWorkPhase) stringResource(R.string.work) else stringResource(R.string.rest),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.round_of, uiState.currentRound, uiState.totalRounds),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (uiState.status == TimerStatus.FINISHED) {
                Text(
                    text = stringResource(R.string.finished),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun TimerControls(
    status: TimerStatus,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onReset: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (status != TimerStatus.IDLE) {
            FilledIconButton(onClick = onReset, modifier = Modifier.size(56.dp)) {
                Icon(Icons.Default.Replay, contentDescription = stringResource(R.string.reset))
            }
        }
        when (status) {
            TimerStatus.IDLE, TimerStatus.FINISHED -> {
                Button(
                    onClick = onStart,
                    modifier = Modifier.height(56.dp).widthIn(min = 120.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.start))
                }
            }
            TimerStatus.RUNNING -> {
                Button(
                    onClick = onPause,
                    modifier = Modifier.height(56.dp).widthIn(min = 120.dp)
                ) {
                    Icon(Icons.Default.Pause, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.pause))
                }
            }
            TimerStatus.PAUSED -> {
                Button(
                    onClick = onResume,
                    modifier = Modifier.height(56.dp).widthIn(min = 120.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.resume))
                }
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}
