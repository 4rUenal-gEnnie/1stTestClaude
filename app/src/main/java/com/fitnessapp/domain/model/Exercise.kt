package com.fitnessapp.domain.model

data class Exercise(
    val id: Long = 0,
    val name: String,
    val description: String,
    val muscleGroup: MuscleGroup,
    val difficulty: Difficulty,
    val instructions: List<String>,
    val isCustom: Boolean = false
)

enum class MuscleGroup(val displayName: String) {
    CHEST("Chest"),
    BACK("Back"),
    SHOULDERS("Shoulders"),
    ARMS("Arms"),
    LEGS("Legs"),
    CORE("Core"),
    CARDIO("Cardio"),
    FULL_BODY("Full Body")
}

enum class Difficulty(val displayName: String) {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced")
}
