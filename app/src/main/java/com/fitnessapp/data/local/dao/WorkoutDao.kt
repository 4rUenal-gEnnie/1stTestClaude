package com.fitnessapp.data.local.dao

import androidx.room.*
import com.fitnessapp.data.local.entity.WorkoutSessionEntity
import com.fitnessapp.data.local.entity.WorkoutSetEntity
import com.fitnessapp.data.local.relation.WorkoutSessionWithSetsRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Query("SELECT * FROM workout_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<WorkoutSessionEntity>>

    @Transaction
    @Query("SELECT * FROM workout_sessions WHERE id = :sessionId")
    fun getSessionWithSets(sessionId: Long): Flow<WorkoutSessionWithSetsRelation?>

    @Insert
    suspend fun insertSession(session: WorkoutSessionEntity): Long

    @Query("UPDATE workout_sessions SET endTime = :endTime WHERE id = :id")
    suspend fun endSession(id: Long, endTime: Long)

    @Insert
    suspend fun insertSet(set: WorkoutSetEntity): Long

    @Query("DELETE FROM workout_sessions WHERE id = :id")
    suspend fun deleteSession(id: Long)

    @Query("SELECT COUNT(*) FROM workout_sessions WHERE endTime IS NOT NULL")
    fun getCompletedWorkoutCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM workout_sets")
    fun getTotalSetsCount(): Flow<Int>

    @Query("SELECT COALESCE(SUM(weightKg * reps), 0.0) FROM workout_sets")
    fun getTotalVolumeKg(): Flow<Float>
}
