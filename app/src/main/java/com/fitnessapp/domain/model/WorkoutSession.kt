package com.fitnessapp.domain.model

data class WorkoutSession(
    val id: Long = 0,
    val routineId: Long? = null,
    val routineName: String? = null,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null,
    val notes: String = ""
)

data class WorkoutSessionWithSets(
    val session: WorkoutSession,
    val sets: List<WorkoutSet>
)

data class WorkoutSet(
    val id: Long = 0,
    val sessionId: Long,
    val exerciseId: Long,
    val exerciseName: String,
    val setNumber: Int,
    val reps: Int,
    val weightKg: Float,
    val completedAt: Long = System.currentTimeMillis()
)

data class WorkoutStats(
    val totalWorkouts: Int = 0,
    val totalSets: Int = 0,
    val totalVolumeKg: Float = 0f
)
