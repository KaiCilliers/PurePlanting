package com.sunrisekcdeveloper.pureplanting.app

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sunrisekcdeveloper.pureplanting.core.database.PurePlantingDatabase
import com.sunrisekcdeveloper.pureplanting.core.design.ui.SnackbarEmitter
import com.sunrisekcdeveloper.pureplanting.domain.notification.NotificationRepository
import com.sunrisekcdeveloper.pureplanting.domain.plant.PlantRepository
import com.sunrisekcdeveloper.pureplanting.app.workers.CompositeWorkerFactory
import com.sunrisekcdeveloper.pureplanting.app.workers.ForgotToWaterReminder
import com.sunrisekcdeveloper.pureplanting.app.workers.SystemNotification
import com.sunrisekcdeveloper.pureplanting.app.workers.WaterPlantReminder
import com.zhuinden.simplestack.GlobalServices
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.rebind
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class PurePlantingApplication : Application(), Configuration.Provider {
    lateinit var globalServices: GlobalServices
        private set

    // Create global dependencies
    private val defaultClock = Clock.systemDefaultZone()
    private val db by lazy { PurePlantingDatabase.getDatabase(this) }
    private val plantRepository by lazy { PlantRepository.Default(db.plantDao()) }
    private val systemNotification by lazy { SystemNotification(applicationContext, plantRepository) }
    private val notificationRepository by lazy { NotificationRepository.Default(db.notificationDao()) }
    private val snackbarEmitter by lazy { SnackbarEmitter() }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(createCompositeWorkerFactory())
            .build()

    override fun onCreate() {
        super.onCreate()

        globalServices = GlobalServices.builder()
            .add(plantRepository)
            .rebind<PlantRepository>(plantRepository)
            .add(notificationRepository)
            .rebind<NotificationRepository>(notificationRepository)
            .add(snackbarEmitter)
            .build()
    }

    private fun createCompositeWorkerFactory(): CompositeWorkerFactory {
        val waterFactory = WaterPlantReminder.Factory(
            plantRepository = plantRepository,
            notificationRepository = notificationRepository,
            systemNotification = systemNotification,
            dao = db.waterWorkerDao(),
            clock = defaultClock,
        )

        val forgotFactory = ForgotToWaterReminder.Factory(
            plantRepository = plantRepository,
            notificationRepository = notificationRepository,
            systemNotification = systemNotification,
            dao = db.forgotWaterWorkerDao(),
            clock = defaultClock,
        )

        return CompositeWorkerFactory(forgotFactory, waterFactory)
    }
}

