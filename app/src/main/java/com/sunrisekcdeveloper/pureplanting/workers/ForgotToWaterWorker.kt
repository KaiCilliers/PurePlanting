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

class ForgotToWaterWorker(
    ctx: Context,
    params: WorkerParameters,
    private val plantCache: PlantCache,
    private val notificationsCache: NotificationsCache,
    private val systemNotification: SystemNotification,
    private val clock: Clock = Clock.systemDefaultZone(),
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return try {

            if (runAttemptCount > 4) {
                return Result.failure()
            }

            val plantsForgotten = plantCache
                .all()
                .filter { it.forgotToWater(LocalDateTime.now(clock)) }

            // on notification tap, open app on specific plant detail screen
            if (plantsForgotten.isNotEmpty()) {
                val notification = NotificationDomain.createForgotToWater(plantsForgotten)
                notificationsCache.save(notification)
                systemNotification.send(notification)
            }

            Result.success()
        } catch (e: Exception) {
            println("Failed to successfully execute ${this::class.simpleName}")
            e.printStackTrace()
            Result.retry()
        }
    }

    class Factory(
        private val plantCache: PlantCache,
        private val notificationsCache: NotificationsCache,
        private val systemNotification: SystemNotification,
        private val clock: Clock = Clock.systemDefaultZone(),
    ) : WorkerFactory() {
        override fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): ListenableWorker {
            return ForgotToWaterWorker(appContext, workerParameters, plantCache, notificationsCache, systemNotification, clock)
        }
    }
}