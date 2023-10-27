package com.sunrisekcdeveloper.pureplanting.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationDomain
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationsCache
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantCache
import com.sunrisekcdeveloper.pureplanting.features.presentation.notifications.PlantNotification
import java.time.Clock
import java.time.LocalDateTime

class DailyPlantReminderWorker(
    ctx: Context,
    params: WorkerParameters,
    private val plantCache: PlantCache,
    private val notificationsCache: NotificationsCache,
    private val clock: Clock = Clock.systemDefaultZone(),
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val plantsThatNeedsWatering = plantCache.allThatNeedsWateringSoon(LocalDateTime.now(clock))

        if (plantsThatNeedsWatering.isNotEmpty()) {
            val notification = NotificationDomain.createWaterSoon()
            notificationsCache.save(notification)
            // on notification tap, open app on plant list screen with upcoming filter selected, i.e. default filter option
        }

        return Result.success()
    }

    class DailyPlantReminderWorkerFactory(
        private val plantCache: PlantCache,
        private val notificationsCache: NotificationsCache,
        private val clock: Clock = Clock.systemDefaultZone(),
    ) : WorkerFactory() {
        override fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): ListenableWorker? {
            return DailyPlantReminderWorker(appContext, workerParameters, plantCache, notificationsCache, clock)
        }
    }

    companion object {
        const val TAG = "DailyPlantReminderWorkerTag"

    }
}