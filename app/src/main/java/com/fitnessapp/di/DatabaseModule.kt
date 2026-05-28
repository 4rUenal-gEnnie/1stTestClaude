package com.fitnessapp.di

import android.content.Context
import androidx.room.Room
import com.fitnessapp.data.local.FitnessDatabase
import com.fitnessapp.data.local.dao.ExerciseDao
import com.fitnessapp.data.local.dao.RoutineDao
import com.fitnessapp.data.local.dao.WorkoutDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFitnessDatabase(
        @ApplicationContext context: Context
    ): FitnessDatabase = Room.databaseBuilder(
        context,
        FitnessDatabase::class.java,
        FitnessDatabase.DATABASE_NAME
    ).build()

    @Provides
    fun provideExerciseDao(db: FitnessDatabase): ExerciseDao = db.exerciseDao()

    @Provides
    fun provideRoutineDao(db: FitnessDatabase): RoutineDao = db.routineDao()

    @Provides
    fun provideWorkoutDao(db: FitnessDatabase): WorkoutDao = db.workoutDao()
}
