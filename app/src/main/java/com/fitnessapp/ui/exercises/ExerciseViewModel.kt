package com.fitnessapp.ui.exercises

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitnessapp.domain.model.Exercise
import com.fitnessapp.domain.model.MuscleGroup
import com.fitnessapp.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExerciseListUiState(
    val exercises: List<Exercise> = emptyList(),
    val searchQuery: String = "",
    val selectedMuscleGroup: MuscleGroup? = null,
    val isLoading: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedMuscleGroup = MutableStateFlow<MuscleGroup?>(null)

    val uiState: StateFlow<ExerciseListUiState> = combine(
        _searchQuery,
        _selectedMuscleGroup
    ) { query, muscleGroup ->
        Pair(query, muscleGroup)
    }.flatMapLatest { (query, muscleGroup) ->
        val exerciseFlow = when {
            query.isNotBlank() -> exerciseRepository.searchExercises(query)
            muscleGroup != null -> exerciseRepository.getExercisesByMuscleGroup(muscleGroup)
            else -> exerciseRepository.getAllExercises()
        }
        exerciseFlow.map { exercises ->
            ExerciseListUiState(
                exercises = exercises,
                searchQuery = query,
                selectedMuscleGroup = muscleGroup
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ExerciseListUiState(isLoading = true)
    )

    private val _selectedExercise = MutableStateFlow<Exercise?>(null)
    val selectedExercise: StateFlow<Exercise?> = _selectedExercise

    init {
        savedStateHandle.get<Long>("exerciseId")?.let { id ->
            if (id > 0) loadExercise(id)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setMuscleGroupFilter(muscleGroup: MuscleGroup?) {
        _selectedMuscleGroup.value = muscleGroup
    }

    fun loadExercise(id: Long) {
        viewModelScope.launch {
            _selectedExercise.value = exerciseRepository.getExerciseById(id)
        }
    }
}
