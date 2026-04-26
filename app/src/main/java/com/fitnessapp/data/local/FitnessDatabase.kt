package com.fitnessapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fitnessapp.data.local.dao.ExerciseDao
import com.fitnessapp.data.local.dao.RoutineDao
import com.fitnessapp.data.local.dao.WorkoutDao
import com.fitnessapp.data.local.entity.*

@Database(
    entities = [
        ExerciseEntity::class,
        RoutineEntity::class,
        RoutineExerciseEntity::class,
        WorkoutSessionEntity::class,
        WorkoutSetEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FitnessDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun routineDao(): RoutineDao
    abstract fun workoutDao(): WorkoutDao

    companion object {
        const val DATABASE_NAME = "fitness_database"
    }
}
