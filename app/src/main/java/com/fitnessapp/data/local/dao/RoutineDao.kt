package com.fitnessapp.data.local.dao

import androidx.room.*
import com.fitnessapp.data.local.entity.RoutineEntity
import com.fitnessapp.data.local.entity.RoutineExerciseEntity
import com.fitnessapp.data.local.relation.RoutineWithExercisesRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {

    @Query("SELECT * FROM routines ORDER BY createdAt DESC")
    fun getAllRoutines(): Flow<List<RoutineEntity>>

    @Transaction
    @Query("SELECT * FROM routines WHERE id = :routineId")
    fun getRoutineWithExercises(routineId: Long): Flow<RoutineWithExercisesRelation?>

    @Insert
    suspend fun insertRoutine(routine: RoutineEntity): Long

    @Update
    suspend fun updateRoutine(routine: RoutineEntity)

    @Delete
    suspend fun deleteRoutine(routine: RoutineEntity)

    @Insert
    suspend fun insertRoutineExercise(item: RoutineExerciseEntity): Long

    @Update
    suspend fun updateRoutineExercise(item: RoutineExerciseEntity)

    @Query("DELETE FROM routine_exercises WHERE id = :id")
    suspend fun deleteRoutineExercise(id: Long)

    @Update
    suspend fun updateRoutineExercises(items: List<RoutineExerciseEntity>)
}
