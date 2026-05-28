package com.fitnessapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fitnessapp.domain.model.Routine

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

fun RoutineEntity.toDomain(): Routine = Routine(
    id = id,
    name = name,
    description = description,
    createdAt = createdAt
)

fun Routine.toEntity(): RoutineEntity = RoutineEntity(
    id = id,
    name = name,
    description = description,
    createdAt = createdAt
)
