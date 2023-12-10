package com.sunrisekcdeveloper.pureplanting.app

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.sunrisekcdeveloper.design.ui.SnackbarEmitter
import com.sunrisekcdeveloper.notification.data.DefaultNotificationRepository
import com.sunrisekcdeveloper.notification.domain.NotificationRepository
import com.sunrisekcdeveloper.plant.data.DefaultPlantRepository
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import com.sunrisekcdeveloper.pureplanting.app.workers.CompositeWorkerFactory
import com.sunrisekcdeveloper.pureplanting.app.workers.ForgotToWaterReminder
import com.sunrisekcdeveloper.pureplanting.app.workers.SystemNotification
import com.sunrisekcdeveloper.pureplanting.app.workers.WaterPlantReminder
import com.zhuinden.simplestack.GlobalServices
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.rebind
import java.time.Clock

class PurePlantingApplication : Application(), Configuration.Provider {
    lateinit var globalServices: GlobalServices
        private set

    // Create global dependencies
    private val systemNotification by lazy { SystemNotification(applicationContext) }
    private val defaultClock = Clock.systemDefaultZone()
    private val db by lazy { PurePlantingDatabase.getDatabase(this) }
    private val plantRepository by lazy { DefaultPlantRepository(db.plantDao()) }
    private val notificationRepository by lazy { DefaultNotificationRepository(db.notificationDao()) }
    // todo make use of snackbar emitter, material 3!
    private val snackbarEmitter by lazy { SnackbarEmitter() }

    private val waterFactory by lazy {
        WaterPlantReminder.Factory(
            plantRepository = plantRepository,
            notificationRepository = notificationRepository,
            systemNotification = systemNotification,
            dao = db.waterWorkerDao(),
            clock = defaultClock,
        )
    }
    private val forgotFactory by lazy {
        ForgotToWaterReminder.Factory(
            plantRepository = plantRepository,
            notificationRepository = notificationRepository,
            systemNotification = systemNotification,
            dao = db.forgotWaterWorkerDao(),
            clock = defaultClock,
        )
    }

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

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(CompositeWorkerFactory(listOf(waterFactory, forgotFactory)))
            .build()
}

