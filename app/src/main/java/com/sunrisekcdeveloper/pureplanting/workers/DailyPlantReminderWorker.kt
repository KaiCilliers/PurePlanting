package com.sunrisekcdeveloper.pureplanting.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationDomain
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationsCache
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantCache
import com.sunrisekcdeveloper.pureplanting.util.SystemNotification
import java.time.Clock
import java.time.LocalDateTime

class DailyPlantReminderWorker(
    ctx: Context,
    params: WorkerParameters,
    private val plantCache: PlantCache,
    private val notificationsCache: NotificationsCache,
    private val systemNotification: SystemNotification,
    private val clock: Clock = Clock.systemDefaultZone(),
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return try {
            val plantsThatNeedsWatering = PlantCache.Smart(plantCache).allThatNeedsWateringSoon(LocalDateTime.now(clock))

            if (plantsThatNeedsWatering.isNotEmpty()) {
                val notification = NotificationDomain.createWaterSoon(plantsThatNeedsWatering.size)
                notificationsCache.save(notification)
                systemNotification.send(notification)
            }

            Result.success()
        } catch (e: Exception) {
            println("Failed to successfully execute ${this::class.simpleName}")
            e.printStackTrace()
            Result.failure()
        }
    }

    class Factory(
        private val plantCache: PlantCache,
        private val notificationsCache: NotificationsCache,
        private val systemNotification: SystemNotification,
        private val clock: Clock = Clock.systemDefaultZone(),
    ) : WorkerFactory() {
        override fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): ListenableWorker? {
            return DailyPlantReminderWorker(appContext, workerParameters, plantCache, notificationsCache, systemNotification, clock)
        }
    }

    companion object {
        const val TAG = "DailyPlantReminderWorkerTag"

    }
}