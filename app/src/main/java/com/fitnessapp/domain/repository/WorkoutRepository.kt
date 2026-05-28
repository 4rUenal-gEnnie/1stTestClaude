package com.fitnessapp.domain.repository

import com.fitnessapp.domain.model.WorkoutSession
import com.fitnessapp.domain.model.WorkoutSessionWithSets
import com.fitnessapp.domain.model.WorkoutSet
import com.fitnessapp.domain.model.WorkoutStats
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getAllSessions(): Flow<List<WorkoutSession>>
    fun getSessionWithSets(id: Long): Flow<WorkoutSessionWithSets?>
    suspend fun startSession(session: WorkoutSession): Long
    suspend fun endSession(id: Long, endTime: Long)
    suspend fun logSet(set: WorkoutSet): Long
    suspend fun deleteSession(id: Long)
    fun getWorkoutStats(): Flow<WorkoutStats>
}
