# FitnessApp

A fitness information Android app built with Kotlin, Jetpack Compose (Material 3), Room, Hilt, and Jetpack Navigation Compose.

## Features

- **Exercise Library** — 35+ pre-loaded exercises organized by muscle group with search and filter
- **Workout Routine Builder** — Create, edit, and delete custom routines; add exercises with sets/reps/rest
- **Workout Tracking** — Log completed workouts, track sets/reps/weight, view history with basic stats
- **Timer / Interval** — Countdown timer and configurable interval timer with audio + vibration alerts running in the foreground

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Repository pattern |
| Local DB | Room 2.6.1 |
| DI | Hilt 2.51 |
| Navigation | Jetpack Navigation Compose 2.7.7 |

## Project Structure

```
app/src/main/java/com/fitnessapp/
├── data/
│   ├── local/
│   │   ├── dao/            # ExerciseDao, RoutineDao, WorkoutDao
│   │   ├── entity/         # Room @Entity classes + mappers
│   │   ├── relation/       # Nested @Relation data classes
│   │   ├── FitnessDatabase.kt
│   │   └── SeedData.kt     # 35 pre-loaded exercises
│   └── repository/         # Repository implementations
├── di/
│   ├── DatabaseModule.kt
│   └── RepositoryModule.kt
├── domain/
│   ├── model/              # Pure domain models (Exercise, Routine, WorkoutSession…)
│   └── repository/         # Repository interfaces
├── service/
│   └── TimerService.kt     # Foreground service for background timer
├── ui/
│   ├── exercises/          # ExerciseListScreen, ExerciseDetailScreen, ExerciseViewModel
│   ├── routines/           # RoutineListScreen, RoutineDetailScreen, RoutineBuilderScreen, RoutineViewModel
│   ├── history/            # HistoryScreen, WorkoutLogScreen, HistoryViewModel
│   ├── timer/              # TimerScreen, TimerViewModel
│   ├── navigation/         # FitnessNavigation (NavHost + BottomNav)
│   └── theme/              # Color, Theme, Type
├── FitnessApplication.kt
└── MainActivity.kt
```

## Requirements

- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 35 (Android 15)
- **Compile SDK**: 35
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 8+

## Setup

1. Clone the repository
2. Open the project root in Android Studio
3. Let Gradle sync and download dependencies (~2–3 minutes on first sync)
4. Connect an Android device or start an emulator (API 26+)
5. Click **Run ▶** (or `Shift+F10`)

The database is seeded automatically on first launch with 35 exercises across 8 muscle groups.

## Architecture Notes

- The **Repository** layer is abstracted behind interfaces in `domain/repository/`, making it straightforward to swap the Room implementation for a remote/cloud data source without touching the UI layer
- **Hilt** manages the dependency graph; `DatabaseModule` provides the Room instance and DAOs, while `RepositoryModule` binds the interface implementations
- All ViewModels use `StateFlow` for observable state; composables collect with `collectAsStateWithLifecycle`
- The **timer foreground service** (`TimerService`) is started/stopped by `TimerViewModel`; the actual countdown logic lives in the ViewModel using coroutines so the UI remains responsive

## Localization

String resources are provided in:
- `res/values/strings.xml` — English (default)
- `res/values-ko/strings.xml` — Korean (한국어)
