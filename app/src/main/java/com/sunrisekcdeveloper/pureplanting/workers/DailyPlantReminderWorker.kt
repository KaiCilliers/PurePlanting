package com.sunrisekcdeveloper.pureplanting.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sunrisekcdeveloper.pureplanting.features.component.PlantCache

class DailyPlantReminderWorker(
    ctx: Context,
    params: WorkerParameters,
    private val plantCache: PlantCache,
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val plantsThatNeedsWatering = plantCache.allThatNeedsWateringSoon()

        // I have access to application context :)
        // would be useful for accessing string resources
        applicationContext

        if (plantsThatNeedsWatering.isNotEmpty()) {
            // create and send notification
            // on notification tap, open app on plant list screen with upcoming filter selected, i.e. default filter option
        }

        return Result.success()
    }

    companion object {
        const val TAG = "DailyPlantReminderWorkerTag"
    }
}