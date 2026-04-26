package com.fitnessapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fitnessapp.R
import com.fitnessapp.ui.exercises.ExerciseDetailScreen
import com.fitnessapp.ui.exercises.ExerciseListScreen
import com.fitnessapp.ui.history.HistoryScreen
import com.fitnessapp.ui.history.WorkoutLogScreen
import com.fitnessapp.ui.routines.RoutineBuilderScreen
import com.fitnessapp.ui.routines.RoutineDetailScreen
import com.fitnessapp.ui.routines.RoutineListScreen
import com.fitnessapp.ui.timer.TimerScreen

sealed class Screen(val route: String) {
    object ExerciseList : Screen("exercises")
    object ExerciseDetail : Screen("exercises/{exerciseId}") {
        fun createRoute(id: Long) = "exercises/$id"
    }
    object RoutineList : Screen("routines")
    object RoutineDetail : Screen("routines/{routineId}") {
        fun createRoute(id: Long) = "routines/$id"
    }
    object RoutineBuilder : Screen("routines/builder/{routineId}") {
        fun createRoute(id: Long = -1L) = "routines/builder/$id"
    }
    object History : Screen("history")
    object WorkoutLog : Screen("history/log/{sessionId}") {
        fun createRoute(id: Long = -1L) = "history/log/$id"
    }
    object Timer : Screen("timer")
}

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val labelRes: Int
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.ExerciseList.route, Icons.Default.FitnessCenter, R.string.nav_exercises),
    BottomNavItem(Screen.RoutineList.route, Icons.Default.List, R.string.nav_routines),
    BottomNavItem(Screen.History.route, Icons.Default.History, R.string.nav_history),
    BottomNavItem(Screen.Timer.route, Icons.Default.Timer, R.string.nav_timer),
)

@Composable
fun FitnessNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = stringResource(item.labelRes)) },
                        label = { Text(stringResource(item.labelRes)) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.ExerciseList.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.ExerciseList.route) {
                ExerciseListScreen(
                    onExerciseClick = { exerciseId ->
                        navController.navigate(Screen.ExerciseDetail.createRoute(exerciseId))
                    }
                )
            }
            composable(
                route = Screen.ExerciseDetail.route,
                arguments = listOf(navArgument("exerciseId") { type = NavType.LongType })
            ) {
                ExerciseDetailScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.RoutineList.route) {
                RoutineListScreen(
                    onRoutineClick = { routineId ->
                        navController.navigate(Screen.RoutineDetail.createRoute(routineId))
                    },
                    onCreateRoutine = {
                        navController.navigate(Screen.RoutineBuilder.createRoute())
                    }
                )
            }
            composable(
                route = Screen.RoutineDetail.route,
                arguments = listOf(navArgument("routineId") { type = NavType.LongType })
            ) {
                RoutineDetailScreen(
                    onBack = { navController.popBackStack() },
                    onEdit = { routineId ->
                        navController.navigate(Screen.RoutineBuilder.createRoute(routineId))
                    },
                    onStartWorkout = { routineId ->
                        navController.navigate(Screen.WorkoutLog.createRoute(-routineId))
                    }
                )
            }
            composable(
                route = Screen.RoutineBuilder.route,
                arguments = listOf(navArgument("routineId") { type = NavType.LongType })
            ) {
                RoutineBuilderScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.History.route) {
                HistoryScreen(
                    onSessionClick = { sessionId ->
                        navController.navigate(Screen.WorkoutLog.createRoute(sessionId))
                    },
                    onNewWorkout = {
                        navController.navigate(Screen.WorkoutLog.createRoute(-1L))
                    }
                )
            }
            composable(
                route = Screen.WorkoutLog.route,
                arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
            ) {
                WorkoutLogScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Timer.route) {
                TimerScreen()
            }
        }
    }
}
