package com.fitnessapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.fitnessapp.data.local.dao.ExerciseDao
import com.fitnessapp.data.local.SeedData
import javax.inject.Inject

@HiltAndroidApp
class FitnessApplication : Application() {

    @Inject
    lateinit var exerciseDao: ExerciseDao

    override fun onCreate() {
        super.onCreate()
        seedDatabaseIfNeeded()
    }

    private fun seedDatabaseIfNeeded() {
        CoroutineScope(Dispatchers.IO).launch {
            if (exerciseDao.getCount() == 0) {
                exerciseDao.insertAll(SeedData.exerciseEntities)
            }
        }
    }
}
