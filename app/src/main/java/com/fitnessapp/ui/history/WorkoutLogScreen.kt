package com.fitnessapp.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
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
import com.fitnessapp.domain.model.Routine
import com.fitnessapp.domain.model.WorkoutSet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutLogScreen(
    onBack: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val sessionWithSets by viewModel.sessionWithSets.collectAsStateWithLifecycle()
    val activeSessionId by viewModel.activeSessionId.collectAsStateWithLifecycle()
    val routines by viewModel.routines.collectAsStateWithLifecycle()
    val allExercises by viewModel.allExercises.collectAsStateWithLifecycle()

    var showStartDialog by remember { mutableStateOf(activeSessionId == null) }
    var showLogSetDialog by remember { mutableStateOf(false) }
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }

    // For new session: pick a routine or free
    var selectedRoutine by remember { mutableStateOf<Routine?>(null) }

    // Set logging state
    var logSetNumber by remember { mutableStateOf("1") }
    var logReps by remember { mutableStateOf("10") }
    var logWeight by remember { mutableStateOf("0") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(sessionWithSets?.session?.routineName ?: stringResource(R.string.workout_log)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    if (activeSessionId != null && sessionWithSets?.session?.endTime == null) {
                        TextButton(onClick = {
                            viewModel.endWorkout()
                            onBack()
                        }) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.finish))
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (activeSessionId != null && sessionWithSets?.session?.endTime == null) {
                FloatingActionButton(onClick = { showLogSetDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.log_set))
                }
            }
        }
    ) { paddingValues ->
        if (showStartDialog) {
            StartWorkoutDialog(
                routines = routines,
                selectedRoutine = selectedRoutine,
                onSelectRoutine = { selectedRoutine = it },
                onStart = {
                    viewModel.startWorkout(selectedRoutine?.id, selectedRoutine?.name)
                    showStartDialog = false
                },
                onDismiss = onBack
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val sets = sessionWithSets?.sets ?: emptyList()
            if (sets.isEmpty() && activeSessionId != null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_sets_logged),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                // Group sets by exercise
                val grouped = sets.groupBy { it.exerciseName }
                grouped.forEach { (exerciseName, exerciseSets) ->
                    item {
                        Text(
                            text = exerciseName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    items(exerciseSets) { set ->
                        SetRow(set = set)
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }

    if (showLogSetDialog) {
        LogSetDialog(
            exercises = allExercises,
            selectedExercise = selectedExercise,
            setNumber = logSetNumber,
            reps = logReps,
            weight = logWeight,
            onExerciseSelect = { selectedExercise = it },
            onSetNumberChange = { logSetNumber = it },
            onRepsChange = { logReps = it },
            onWeightChange = { logWeight = it },
            onConfirm = {
                selectedExercise?.let { exercise ->
                    viewModel.logSet(
                        exerciseId = exercise.id,
                        exerciseName = exercise.name,
                        setNumber = logSetNumber.toIntOrNull() ?: 1,
                        reps = logReps.toIntOrNull() ?: 10,
                        weightKg = logWeight.toFloatOrNull() ?: 0f
                    )
                }
                showLogSetDialog = false
                selectedExercise = null
                logSetNumber = "1"
            },
            onDismiss = {
                showLogSetDialog = false
                selectedExercise = null
            }
        )
    }
}

@Composable
private fun SetRow(set: WorkoutSet) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.set_number, set.setNumber),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${set.reps} reps × ${set.weightKg} kg",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StartWorkoutDialog(
    routines: List<Routine>,
    selectedRoutine: Routine?,
    onSelectRoutine: (Routine?) -> Unit,
    onStart: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.start_workout)) },
        text = {
            LazyColumn(
                modifier = Modifier.heightIn(max = 300.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedRoutine == null,
                            onClick = { onSelectRoutine(null) }
                        )
                        Text(
                            text = stringResource(R.string.freeform_workout),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
                items(routines) { routine ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedRoutine?.id == routine.id,
                            onClick = { onSelectRoutine(routine) }
                        )
                        Text(
                            text = routine.name,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onStart) { Text(stringResource(R.string.start)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

@Composable
private fun LogSetDialog(
    exercises: List<Exercise>,
    selectedExercise: Exercise?,
    setNumber: String,
    reps: String,
    weight: String,
    onExerciseSelect: (Exercise) -> Unit,
    onSetNumberChange: (String) -> Unit,
    onRepsChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.log_set)) },
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
                                    .clickableCompat { onExerciseSelect(exercise) },
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
                            value = setNumber,
                            onValueChange = onSetNumberChange,
                            label = { Text(stringResource(R.string.set_hash)) },
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
                        value = weight,
                        onValueChange = onWeightChange,
                        label = { Text(stringResource(R.string.weight_kg)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            if (selectedExercise != null) {
                TextButton(onClick = onConfirm) { Text(stringResource(R.string.log)) }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

// Extension to avoid import conflict
private fun Modifier.clickableCompat(onClick: () -> Unit): Modifier =
    this.then(Modifier.clickable(onClick = onClick))
