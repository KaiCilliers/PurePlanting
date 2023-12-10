package com.sunrisekcdeveloper.pureplanting.app.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.sunrisekcdeveloper.notification.domain.Notification
import com.sunrisekcdeveloper.notification.domain.NotificationRepository
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import java.time.Clock
import java.time.LocalDateTime
import java.time.LocalTime

class WaterPlantReminder(
    ctx: Context,
    params: WorkerParameters,
    private val plantRepository: PlantRepository,
    private val notificationRepository: NotificationRepository,
    private val dao: WateringWorkerResultStatusDao,
    private val systemNotification: SystemNotification,
    private val clock: Clock = Clock.systemDefaultZone(),
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return try {
            val today = LocalDateTime.now(clock)
            val now = LocalTime.now(clock)
            val plantsThatNeedsWatering = plantRepository
                .all()
                .filter { it.needsWaterToday(today) }
                .filter { it.wateringInfo.time.isBefore(now.plusMinutes(15)) }

            var notificationId: String? = null

            // on tap open app on plants screen
            if (plantsThatNeedsWatering.isNotEmpty()) {
                val notification = Notification.createWaterSoon(plantsThatNeedsWatering.map { it.id })
                notificationRepository.save(notification)
                systemNotification.send(notification)
                notificationId = notification.id
            }

            dao.insert(
                WateringWorkerResultStatusEntity(
                    status = "Success Plant Water using composite worker",
                    amountOfPlants = plantsThatNeedsWatering.size,
                    notificationId = notificationId,
                    atDate = LocalDateTime.now(clock)
                )
            )

            Result.success()
        } catch (e: Exception) {
            dao.insert(
                WateringWorkerResultStatusEntity(
                    status = "Failed! Plant Water using composite worker",
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
        private val plantRepository: PlantRepository,
        private val notificationRepository: NotificationRepository,
        private val systemNotification: SystemNotification,
        private val dao: WateringWorkerResultStatusDao,
        private val clock: Clock = Clock.systemDefaultZone(),
    ) : WorkerFactory() {
        override fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): ListenableWorker {
            return WaterPlantReminder(appContext, workerParameters, plantRepository, notificationRepository, dao, systemNotification, clock)
        }
    }

    companion object {
        const val TAG = "WaterPlantReminderTag"
    }
}