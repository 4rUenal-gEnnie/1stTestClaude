package com.fitnessapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fitnessapp.domain.model.WorkoutSession

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val routineId: Long? = null,
    val routineName: String? = null,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null,
    val notes: String = ""
)

fun WorkoutSessionEntity.toDomain(): WorkoutSession = WorkoutSession(
    id = id,
    routineId = routineId,
    routineName = routineName,
    startTime = startTime,
    endTime = endTime,
    notes = notes
)

fun WorkoutSession.toEntity(): WorkoutSessionEntity = WorkoutSessionEntity(
    id = id,
    routineId = routineId,
    routineName = routineName,
    startTime = startTime,
    endTime = endTime,
    notes = notes
)
