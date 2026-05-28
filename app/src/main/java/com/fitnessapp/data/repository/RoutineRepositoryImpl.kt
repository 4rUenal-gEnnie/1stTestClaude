package com.fitnessapp.data.repository

import com.fitnessapp.data.local.dao.RoutineDao
import com.fitnessapp.data.local.entity.RoutineExerciseEntity
import com.fitnessapp.data.local.entity.toDomain
import com.fitnessapp.data.local.entity.toEntity
import com.fitnessapp.data.local.relation.toDomain
import com.fitnessapp.domain.model.Routine
import com.fitnessapp.domain.model.RoutineExerciseItem
import com.fitnessapp.domain.model.RoutineWithExercises
import com.fitnessapp.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoutineRepositoryImpl @Inject constructor(
    private val routineDao: RoutineDao
) : RoutineRepository {

    override fun getAllRoutines(): Flow<List<Routine>> =
        routineDao.getAllRoutines().map { it.map { entity -> entity.toDomain() } }

    override fun getRoutineWithExercises(id: Long): Flow<RoutineWithExercises?> =
        routineDao.getRoutineWithExercises(id).map { it?.toDomain() }

    override suspend fun insertRoutine(routine: Routine): Long =
        routineDao.insertRoutine(routine.toEntity())

    override suspend fun updateRoutine(routine: Routine) =
        routineDao.updateRoutine(routine.toEntity())

    override suspend fun deleteRoutine(routine: Routine) =
        routineDao.deleteRoutine(routine.toEntity())

    override suspend fun addExerciseToRoutine(item: RoutineExerciseItem): Long =
        routineDao.insertRoutineExercise(item.toEntity())

    override suspend fun updateRoutineExercise(item: RoutineExerciseItem) =
        routineDao.updateRoutineExercise(item.toEntity())

    override suspend fun removeExerciseFromRoutine(id: Long) =
        routineDao.deleteRoutineExercise(id)

    override suspend fun reorderRoutineExercises(items: List<RoutineExerciseItem>) =
        routineDao.updateRoutineExercises(items.map { it.toEntity() })

    private fun RoutineExerciseItem.toEntity(): RoutineExerciseEntity = RoutineExerciseEntity(
        id = id,
        routineId = routineId,
        exerciseId = exercise.id,
        sets = sets,
        reps = reps,
        restSeconds = restSeconds,
        orderIndex = orderIndex
    )
}
