package com.fitnessapp.domain.model

data class Routine(
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class RoutineWithExercises(
    val routine: Routine,
    val exercises: List<RoutineExerciseItem>
)

data class RoutineExerciseItem(
    val id: Long = 0,
    val routineId: Long,
    val exercise: Exercise,
    val sets: Int,
    val reps: Int,
    val restSeconds: Int,
    val orderIndex: Int
)
