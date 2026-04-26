package com.fitnessapp.ui.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitnessapp.domain.model.*
import com.fitnessapp.domain.repository.ExerciseRepository
import com.fitnessapp.domain.repository.RoutineRepository
import com.fitnessapp.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val routineRepository: RoutineRepository,
    private val exerciseRepository: ExerciseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val sessions: StateFlow<List<WorkoutSession>> = workoutRepository.getAllSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val stats: StateFlow<WorkoutStats> = workoutRepository.getWorkoutStats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), WorkoutStats())

    val routines: StateFlow<List<Routine>> = routineRepository.getAllRoutines()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val allExercises: StateFlow<List<Exercise>> = exerciseRepository.getAllExercises()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // For active session detail / logging
    private val sessionId: Long = savedStateHandle.get<Long>("sessionId") ?: -1L

    val sessionWithSets: StateFlow<WorkoutSessionWithSets?> = if (sessionId > 0) {
        workoutRepository.getSessionWithSets(sessionId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    } else {
        MutableStateFlow(null)
    }

    private val _activeSessionId = MutableStateFlow<Long?>(if (sessionId > 0) sessionId else null)
    val activeSessionId: StateFlow<Long?> = _activeSessionId

    fun startWorkout(routineId: Long?, routineName: String?) {
        viewModelScope.launch {
            val id = workoutRepository.startSession(
                WorkoutSession(
                    routineId = routineId,
                    routineName = routineName
                )
            )
            _activeSessionId.value = id
        }
    }

    fun endWorkout() {
        viewModelScope.launch {
            _activeSessionId.value?.let { id ->
                workoutRepository.endSession(id, System.currentTimeMillis())
            }
        }
    }

    fun logSet(exerciseId: Long, exerciseName: String, setNumber: Int, reps: Int, weightKg: Float) {
        viewModelScope.launch {
            val id = _activeSessionId.value ?: return@launch
            workoutRepository.logSet(
                WorkoutSet(
                    sessionId = id,
                    exerciseId = exerciseId,
                    exerciseName = exerciseName,
                    setNumber = setNumber,
                    reps = reps,
                    weightKg = weightKg
                )
            )
        }
    }

    fun deleteSession(id: Long) {
        viewModelScope.launch {
            workoutRepository.deleteSession(id)
        }
    }
}
