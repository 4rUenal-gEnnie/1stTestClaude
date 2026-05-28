package com.fitnessapp.ui.routines

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fitnessapp.R
import com.fitnessapp.domain.model.Exercise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineBuilderScreen(
    onBack: () -> Unit,
    viewModel: RoutineViewModel = hiltViewModel()
) {
    val isEditMode = remember {
        // routineId is injected into ViewModel via SavedStateHandle
        // If > 0 it is edit mode, if == -1 it is create mode
        false
    }
    val routineWithExercises by viewModel.routineWithExercises.collectAsStateWithLifecycle()
    val allExercises by viewModel.allExercises.collectAsStateWithLifecycle()
    val savedRoutineId by viewModel.savedRoutineId.collectAsStateWithLifecycle()

    var routineName by remember(routineWithExercises) {
        mutableStateOf(routineWithExercises?.routine?.name ?: "")
    }
    var routineDescription by remember(routineWithExercises) {
        mutableStateOf(routineWithExercises?.routine?.description ?: "")
    }
    var showExercisePicker by remember { mutableStateOf(false) }
    var exercisePickerSets by remember { mutableStateOf("3") }
    var exercisePickerReps by remember { mutableStateOf("10") }
    var exercisePickerRest by remember { mutableStateOf("60") }
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
    var createdRoutineId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(savedRoutineId) {
        savedRoutineId?.let { id ->
            createdRoutineId = id
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (routineWithExercises != null)
                            stringResource(R.string.edit_routine)
                        else
                            stringResource(R.string.new_routine)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (routineName.isNotBlank()) {
                                val existingId = routineWithExercises?.routine?.id
                                if (existingId != null && existingId > 0) {
                                    viewModel.updateRoutine(existingId, routineName, routineDescription)
                                    onBack()
                                } else {
                                    viewModel.createRoutine(routineName, routineDescription)
                                }
                            }
                        },
                        enabled = routineName.isNotBlank()
                    ) {
                        Icon(Icons.Default.Check, contentDescription = stringResource(R.string.save))
                    }
                }
            )
        }
    ) { paddingValues ->
        LaunchedEffect(savedRoutineId) {
            if (savedRoutineId != null && routineWithExercises == null) {
                onBack()
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OutlinedTextField(
                    value = routineName,
                    onValueChange = { routineName = it },
                    label = { Text(stringResource(R.string.routine_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            item {
                OutlinedTextField(
                    value = routineDescription,
                    onValueChange = { routineDescription = it },
                    label = { Text(stringResource(R.string.routine_description)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }

            val exercises = routineWithExercises?.exercises ?: emptyList()
            val activeRoutineId = routineWithExercises?.routine?.id ?: createdRoutineId

            if (activeRoutineId != null) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.exercises),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = { showExercisePicker = true }) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.add_exercise))
                        }
                    }
                }

                items(exercises, key = { it.id }) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.exercise.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = stringResource(R.string.sets_reps_rest, item.sets, item.reps, item.restSeconds),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = { viewModel.removeExerciseFromRoutine(item.id) }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.remove),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            } else {
                item {
                    Text(
                        text = stringResource(R.string.save_to_add_exercises),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    if (showExercisePicker) {
        ExercisePickerDialog(
            exercises = allExercises,
            selectedExercise = selectedExercise,
            sets = exercisePickerSets,
            reps = exercisePickerReps,
            rest = exercisePickerRest,
            onSetsChange = { exercisePickerSets = it },
            onRepsChange = { exercisePickerReps = it },
            onRestChange = { exercisePickerRest = it },
            onExerciseSelect = { selectedExercise = it },
            onConfirm = {
                val exercise = selectedExercise
                val activeId = routineWithExercises?.routine?.id ?: createdRoutineId
                if (exercise != null && activeId != null) {
                    viewModel.addExerciseToRoutine(
                        routineId = activeId,
                        exercise = exercise,
                        sets = exercisePickerSets.toIntOrNull() ?: 3,
                        reps = exercisePickerReps.toIntOrNull() ?: 10,
                        restSeconds = exercisePickerRest.toIntOrNull() ?: 60,
                        orderIndex = (routineWithExercises?.exercises?.size ?: 0)
                    )
                    showExercisePicker = false
                    selectedExercise = null
                }
            },
            onDismiss = {
                showExercisePicker = false
                selectedExercise = null
            }
        )
    }
}

@Composable
private fun ExercisePickerDialog(
    exercises: List<Exercise>,
    selectedExercise: Exercise?,
    sets: String,
    reps: String,
    rest: String,
    onSetsChange: (String) -> Unit,
    onRepsChange: (String) -> Unit,
    onRestChange: (String) -> Unit,
    onExerciseSelect: (Exercise) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_exercise)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (selectedExercise == null) {
                    LazyColumn(
                        modifier = Modifier.height(250.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(exercises) { exercise ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onExerciseSelect(exercise) },
                                shape = MaterialTheme.shapes.small,
                                tonalElevation = 2.dp
                            ) {
                                Text(
                                    text = exercise.name,
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = selectedExercise.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = sets,
                            onValueChange = onSetsChange,
                            label = { Text(stringResource(R.string.sets)) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = reps,
                            onValueChange = onRepsChange,
                            label = { Text(stringResource(R.string.reps)) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                    OutlinedTextField(
                        value = rest,
                        onValueChange = onRestChange,
                        label = { Text(stringResource(R.string.rest_seconds)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            if (selectedExercise != null) {
                TextButton(onClick = onConfirm) {
                    Text(stringResource(R.string.add))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
