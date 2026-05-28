package com.fitnessapp.data.repository

import com.fitnessapp.data.local.dao.ExerciseDao
import com.fitnessapp.data.local.entity.toDomain
import com.fitnessapp.data.local.entity.toEntity
import com.fitnessapp.domain.model.Exercise
import com.fitnessapp.domain.model.MuscleGroup
import com.fitnessapp.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseDao: ExerciseDao
) : ExerciseRepository {

    override fun getAllExercises(): Flow<List<Exercise>> =
        exerciseDao.getAllExercises().map { it.map { entity -> entity.toDomain() } }

    override fun getExercisesByMuscleGroup(muscleGroup: MuscleGroup): Flow<List<Exercise>> =
        exerciseDao.getByMuscleGroup(muscleGroup.name).map { it.map { entity -> entity.toDomain() } }

    override fun searchExercises(query: String): Flow<List<Exercise>> =
        exerciseDao.searchExercises(query).map { it.map { entity -> entity.toDomain() } }

    override suspend fun getExerciseById(id: Long): Exercise? =
        exerciseDao.getById(id)?.toDomain()

    override suspend fun insertExercise(exercise: Exercise): Long =
        exerciseDao.insert(exercise.toEntity())

    override suspend fun updateExercise(exercise: Exercise) =
        exerciseDao.update(exercise.toEntity())

    override suspend fun deleteExercise(exercise: Exercise) =
        exerciseDao.delete(exercise.toEntity())
}
