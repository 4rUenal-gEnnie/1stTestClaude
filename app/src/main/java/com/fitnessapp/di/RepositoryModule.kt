package com.fitnessapp.di

import com.fitnessapp.data.repository.ExerciseRepositoryImpl
import com.fitnessapp.data.repository.RoutineRepositoryImpl
import com.fitnessapp.data.repository.WorkoutRepositoryImpl
import com.fitnessapp.domain.repository.ExerciseRepository
import com.fitnessapp.domain.repository.RoutineRepository
import com.fitnessapp.domain.repository.WorkoutRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExerciseRepository(impl: ExerciseRepositoryImpl): ExerciseRepository

    @Binds
    @Singleton
    abstract fun bindRoutineRepository(impl: RoutineRepositoryImpl): RoutineRepository

    @Binds
    @Singleton
    abstract fun bindWorkoutRepository(impl: WorkoutRepositoryImpl): WorkoutRepository
}
