package com.sunrisekcdeveloper.reminders

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.sunrisekcdeveloper.notification.NotificationCache
import com.sunrisekcdeveloper.notification.NotificationDomain
import com.sunrisekcdeveloper.plant.PlantCache
import java.time.Clock
import java.time.LocalDateTime

class WaterPlantReminder(
    ctx: Context,
    params: WorkerParameters,
    private val plantCache: PlantCache,
    private val notificationCache: NotificationCache,
    private val dao: WateringWorkerResultStatusDao,
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
                val notification = NotificationDomain.createWaterSoon(plantsThatNeedsWatering.map { it.id })
                notificationCache.save(notification)
                systemNotification.send(notification)
                notificationId = notification.id
            }

            dao.insert(
                WateringWorkerResultStatusEntity(
                    status = "Success",
                    amountOfPlants = plantsThatNeedsWatering.size,
                    notificationId = notificationId,
                    atDate = LocalDateTime.now()
                )
            )

            Result.success()
        } catch (e: Exception) {
            dao.insert(
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
        private val dao: WateringWorkerResultStatusDao,
        private val clock: Clock = Clock.systemDefaultZone(),
    ) : WorkerFactory() {
        override fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): ListenableWorker {
            return WaterPlantReminder(appContext, workerParameters, plantCache, notificationCache, dao, systemNotification, clock)
        }
    }

    companion object {
        const val TAG = "WaterPlantReminderTag"
        const val PERIODIC_INTERVAL_MINUTES = 15L
    }
}