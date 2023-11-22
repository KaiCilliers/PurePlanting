package com.sunrisekcdeveloper.pureplanting.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.sunrisekcdeveloper.pureplanting.features.component.PurePlantingDatabase
import com.sunrisekcdeveloper.pureplanting.features.component.WateringWorkerResultStatusEntity
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationDomain
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationCache
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantCache
import com.sunrisekcdeveloper.pureplanting.util.SystemNotification
import java.time.Clock
import java.time.LocalDateTime

// todo rename remove daily
class DailyPlantReminderWorker(
    ctx: Context,
    params: WorkerParameters,
    private val plantCache: PlantCache,
    private val notificationCache: NotificationCache,
    private val db: PurePlantingDatabase,
    private val systemNotification: SystemNotification,
    private val clock: Clock = Clock.systemDefaultZone(),
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return try {
            val plantsThatNeedsWatering = PlantCache
                .Smart(plantCache)
                .allThatNeedsWateringBefore(LocalDateTime.now(clock).plusMinutes(PERIODIC_INTERVAL_MINUTES))

            var notificationId: String? = null

            // on tap open app on plants screen
            if (plantsThatNeedsWatering.isNotEmpty()) {
                val notification = NotificationDomain.createWaterSoon(plantsThatNeedsWatering)
                notificationCache.save(notification)
                systemNotification.send(notification)
                notificationId = notification.id
            }

            db.notificationDao2().insert(
                WateringWorkerResultStatusEntity(
                    status = "Success",
                    amountOfPlants = plantsThatNeedsWatering.size,
                    notificationId = notificationId,
                    atDate = LocalDateTime.now()
                )
            )

            Result.success()
        } catch (e: Exception) {
            db.notificationDao2().insert(
                WateringWorkerResultStatusEntity(
                    status = "Success",
                    amountOfPlants = 0,
                    notificationId = null,
                    atDate = LocalDateTime.now(),
                    failureMsg = e.message
                )
            )
            println("Failed to successfully execute ${this::class.simpleName}")
            e.printStackTrace()
            Result.failure()
        }
    }

    class Factory(
        private val plantCache: PlantCache,
        private val notificationCache: NotificationCache,
        private val systemNotification: SystemNotification,
        private val db: PurePlantingDatabase,
        private val clock: Clock = Clock.systemDefaultZone(),
    ) : WorkerFactory() {
        override fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): ListenableWorker {
            return DailyPlantReminderWorker(appContext, workerParameters, plantCache, notificationCache, db, systemNotification, clock)
        }
    }

    companion object {
        const val TAG = "DailyPlantReminderWorkerTag"
        const val PERIODIC_INTERVAL_MINUTES = 15L
    }
}