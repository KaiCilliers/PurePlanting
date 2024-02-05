package com.sunrisekcdeveloper.pureplanting.app.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

class CompositeWorkerFactory(
    private val forgotToWaterReminderFactory: ForgotToWaterReminder.Factory,
    private val waterPlantReminderFactory: WaterPlantReminder.Factory,
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when(workerClassName) {
            ForgotToWaterReminder::class.java.name -> forgotToWaterReminderFactory.createWorker(appContext, workerClassName, workerParameters)
            WaterPlantReminder::class.java.name -> waterPlantReminderFactory.createWorker(appContext, workerClassName, workerParameters)
            else -> null
        }
    }
}