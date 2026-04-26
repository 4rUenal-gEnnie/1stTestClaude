package com.fitnessapp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.fitnessapp.data.local.entity.ExerciseEntity
import com.fitnessapp.data.local.entity.RoutineEntity
import com.fitnessapp.data.local.entity.RoutineExerciseEntity
import com.fitnessapp.data.local.entity.WorkoutSessionEntity
import com.fitnessapp.data.local.entity.WorkoutSetEntity
import com.fitnessapp.data.local.entity.toDomain
import com.fitnessapp.domain.model.RoutineExerciseItem
import com.fitnessapp.domain.model.RoutineWithExercises
import com.fitnessapp.domain.model.WorkoutSessionWithSets

data class RoutineExerciseWithExercise(
    @Embedded val routineExercise: RoutineExerciseEntity,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "id"
    )
    val exercise: ExerciseEntity
)

data class RoutineWithExercisesRelation(
    @Embedded val routine: RoutineEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "routineId",
        entity = RoutineExerciseEntity::class
    )
    val routineExercises: List<RoutineExerciseWithExercise>
)

data class WorkoutSessionWithSetsRelation(
    @Embedded val session: WorkoutSessionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "sessionId"
    )
    val sets: List<WorkoutSetEntity>
)

fun RoutineExerciseWithExercise.toDomain(): RoutineExerciseItem = RoutineExerciseItem(
    id = routineExercise.id,
    routineId = routineExercise.routineId,
    exercise = exercise.toDomain(),
    sets = routineExercise.sets,
    reps = routineExercise.reps,
    restSeconds = routineExercise.restSeconds,
    orderIndex = routineExercise.orderIndex
)

fun RoutineWithExercisesRelation.toDomain(): RoutineWithExercises = RoutineWithExercises(
    routine = routine.toDomain(),
    exercises = routineExercises.sortedBy { it.routineExercise.orderIndex }.map { it.toDomain() }
)

fun WorkoutSessionWithSetsRelation.toDomain(): WorkoutSessionWithSets = WorkoutSessionWithSets(
    session = session.toDomain(),
    sets = sets.sortedBy { it.completedAt }.map { it.toDomain() }
)
