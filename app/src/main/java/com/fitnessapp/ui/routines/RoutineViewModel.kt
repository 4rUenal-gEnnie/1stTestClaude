package com.fitnessapp.ui.routines

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitnessapp.domain.model.Exercise
import com.fitnessapp.domain.model.Routine
import com.fitnessapp.domain.model.RoutineExerciseItem
import com.fitnessapp.domain.model.RoutineWithExercises
import com.fitnessapp.domain.repository.ExerciseRepository
import com.fitnessapp.domain.repository.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val exerciseRepository: ExerciseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val routines: StateFlow<List<Routine>> = routineRepository.getAllRoutines()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val routineId: Long = savedStateHandle.get<Long>("routineId") ?: -1L

    val routineWithExercises: StateFlow<RoutineWithExercises?> = if (routineId > 0) {
        routineRepository.getRoutineWithExercises(routineId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    } else {
        MutableStateFlow(null)
    }

    val allExercises: StateFlow<List<Exercise>> = exerciseRepository.getAllExercises()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _savedRoutineId = MutableStateFlow<Long?>(null)
    val savedRoutineId: StateFlow<Long?> = _savedRoutineId

    fun createRoutine(name: String, description: String) {
        viewModelScope.launch {
            val id = routineRepository.insertRoutine(
                Routine(name = name, description = description)
            )
            _savedRoutineId.value = id
        }
    }

    fun updateRoutine(id: Long, name: String, description: String) {
        viewModelScope.launch {
            routineRepository.updateRoutine(Routine(id = id, name = name, description = description))
        }
    }

    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            routineRepository.deleteRoutine(routine)
        }
    }

    fun addExerciseToRoutine(routineId: Long, exercise: Exercise, sets: Int, reps: Int, restSeconds: Int, orderIndex: Int) {
        viewModelScope.launch {
            routineRepository.addExerciseToRoutine(
                RoutineExerciseItem(
                    routineId = routineId,
                    exercise = exercise,
                    sets = sets,
                    reps = reps,
                    restSeconds = restSeconds,
                    orderIndex = orderIndex
                )
            )
        }
    }

    fun updateRoutineExercise(item: RoutineExerciseItem) {
        viewModelScope.launch {
            routineRepository.updateRoutineExercise(item)
        }
    }

    fun removeExerciseFromRoutine(id: Long) {
        viewModelScope.launch {
            routineRepository.removeExerciseFromRoutine(id)
        }
    }

    fun reorderExercises(items: List<RoutineExerciseItem>) {
        viewModelScope.launch {
            val reindexed = items.mapIndexed { index, item -> item.copy(orderIndex = index) }
            routineRepository.reorderRoutineExercises(reindexed)
        }
    }
}
