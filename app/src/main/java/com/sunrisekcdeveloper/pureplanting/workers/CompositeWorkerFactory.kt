package com.sunrisekcdeveloper.pureplanting.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

class CompositeWorkerFactory(
    private val factories: List<WorkerFactory>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        for (factory in factories) {
            val worker = factory.createWorker(appContext, workerClassName, workerParameters)
            if (worker != null) {
                return worker
            }
        }
        return null
    }
}