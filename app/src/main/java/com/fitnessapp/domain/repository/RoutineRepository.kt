package com.fitnessapp.domain.repository

import com.fitnessapp.domain.model.Routine
import com.fitnessapp.domain.model.RoutineExerciseItem
import com.fitnessapp.domain.model.RoutineWithExercises
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    fun getAllRoutines(): Flow<List<Routine>>
    fun getRoutineWithExercises(id: Long): Flow<RoutineWithExercises?>
    suspend fun insertRoutine(routine: Routine): Long
    suspend fun updateRoutine(routine: Routine)
    suspend fun deleteRoutine(routine: Routine)
    suspend fun addExerciseToRoutine(item: RoutineExerciseItem): Long
    suspend fun updateRoutineExercise(item: RoutineExerciseItem)
    suspend fun removeExerciseFromRoutine(id: Long)
    suspend fun reorderRoutineExercises(items: List<RoutineExerciseItem>)
}
