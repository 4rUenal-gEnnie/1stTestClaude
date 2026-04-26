package com.fitnessapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fitnessapp.domain.model.Difficulty
import com.fitnessapp.domain.model.Exercise
import com.fitnessapp.domain.model.MuscleGroup

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val muscleGroup: String,
    val difficulty: String,
    val instructions: String,
    val isCustom: Boolean = false
)

fun ExerciseEntity.toDomain(): Exercise = Exercise(
    id = id,
    name = name,
    description = description,
    muscleGroup = MuscleGroup.valueOf(muscleGroup),
    difficulty = Difficulty.valueOf(difficulty),
    instructions = instructions.split("|").filter { it.isNotEmpty() },
    isCustom = isCustom
)

fun Exercise.toEntity(): ExerciseEntity = ExerciseEntity(
    id = id,
    name = name,
    description = description,
    muscleGroup = muscleGroup.name,
    difficulty = difficulty.name,
    instructions = instructions.joinToString("|"),
    isCustom = isCustom
)
