package com.sunrisekcdeveloper.pureplanting.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantCache

class DailyPlantReminderWorkerFactory(private val plantCache: PlantCache) : WorkerFactory() {
    override fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): ListenableWorker? {
        return DailyPlantReminderWorker(appContext, workerParameters, plantCache)
    }
}