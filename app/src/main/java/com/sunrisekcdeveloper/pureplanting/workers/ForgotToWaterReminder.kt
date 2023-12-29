package com.sunrisekcdeveloper.pureplanting.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.sunrisekcdeveloper.notification.domain.Notification
import com.sunrisekcdeveloper.notification.domain.NotificationRepository
import com.sunrisekcdeveloper.notification.domain.PlantTag
import com.sunrisekcdeveloper.plant.PlantRepository
import java.time.Clock
import java.time.LocalDateTime

class ForgotToWaterReminder(
    ctx: Context,
    params: WorkerParameters,
    private val plantRepository: PlantRepository,
    private val notificationRepository: NotificationRepository,
    private val systemNotification: SystemNotification,
    private val dao: ForgotWaterWorkerResultStatusDao,
    private val clock: Clock = Clock.systemDefaultZone(),
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return try {

            if (runAttemptCount > 4) {
                return Result.failure()
            }

            val plantsForgotten = plantRepository
                .all()
                .filter { plant -> plant.missedLatestWateringDate(LocalDateTime.now(clock)) }

            var notificationId: String? = null

            // on notification tap, open app on specific plant detail screen
            if (plantsForgotten.isNotEmpty()) {
                val notification = Notification.createForgotToWater(plantsForgotten.map { PlantTag(it.id, it.details.name) })
                notificationRepository.save(notification)
                systemNotification.send(notification)
                notificationId = notification.id
            }

            dao.insert(
                ForgotWaterWorkerResultStatusEntity(
                    status = "+ Success Forgot using composite worker",
                    amountOfPlants = plantsForgotten.size,
                    notificationId = notificationId,
                    atDate = LocalDateTime.now()
                )
            )

            Result.success()
        } catch (e: Exception) {
            dao.insert(
                ForgotWaterWorkerResultStatusEntity(
                    status = "+ Failed! Forgot using composite worker",
                    amountOfPlants = 0,
                    notificationId = null,
                    atDate = LocalDateTime.now(),
                    failureMsg = e.message
                )
            )
            println("+ Failed to successfully execute ${this::class.simpleName}")
            e.printStackTrace()
            Result.failure()
        }
    }

    class Factory(
        private val plantRepository: PlantRepository,
        private val notificationRepository: NotificationRepository,
        private val systemNotification: SystemNotification,
        private val dao: ForgotWaterWorkerResultStatusDao,
        private val clock: Clock = Clock.systemDefaultZone(),
    ) : WorkerFactory() {
        override fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): ListenableWorker {
            return ForgotToWaterReminder(appContext, workerParameters, plantRepository, notificationRepository, systemNotification, dao, clock)
        }
    }

    companion object {
        const val TAG = "ForgotToWaterReminder"
    }
}