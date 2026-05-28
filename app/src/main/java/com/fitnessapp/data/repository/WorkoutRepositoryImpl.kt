package com.fitnessapp.data.repository

import com.fitnessapp.data.local.dao.WorkoutDao
import com.fitnessapp.data.local.entity.toDomain
import com.fitnessapp.data.local.entity.toEntity
import com.fitnessapp.data.local.relation.toDomain
import com.fitnessapp.domain.model.WorkoutSession
import com.fitnessapp.domain.model.WorkoutSessionWithSets
import com.fitnessapp.domain.model.WorkoutSet
import com.fitnessapp.domain.model.WorkoutStats
import com.fitnessapp.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao
) : WorkoutRepository {

    override fun getAllSessions(): Flow<List<WorkoutSession>> =
        workoutDao.getAllSessions().map { it.map { entity -> entity.toDomain() } }

    override fun getSessionWithSets(id: Long): Flow<WorkoutSessionWithSets?> =
        workoutDao.getSessionWithSets(id).map { it?.toDomain() }

    override suspend fun startSession(session: WorkoutSession): Long =
        workoutDao.insertSession(session.toEntity())

    override suspend fun endSession(id: Long, endTime: Long) =
        workoutDao.endSession(id, endTime)

    override suspend fun logSet(set: WorkoutSet): Long =
        workoutDao.insertSet(set.toEntity())

    override suspend fun deleteSession(id: Long) =
        workoutDao.deleteSession(id)

    override fun getWorkoutStats(): Flow<WorkoutStats> =
        combine(
            workoutDao.getCompletedWorkoutCount(),
            workoutDao.getTotalSetsCount(),
            workoutDao.getTotalVolumeKg()
        ) { workouts, sets, volume ->
            WorkoutStats(
                totalWorkouts = workouts,
                totalSets = sets,
                totalVolumeKg = volume
            )
        }
}
