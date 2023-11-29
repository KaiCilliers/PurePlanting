package com.sunrisekcdeveloper.pureplanting.app

import android.app.Application
import androidx.work.Configuration
import com.sunrisekcdeveloper.notification.LocalDatabaseNotificationCache
import com.sunrisekcdeveloper.notification.NotificationCache
import com.sunrisekcdeveloper.plant.LocalDatabasePlantCache
import com.sunrisekcdeveloper.plant.PlantCache
import com.sunrisekcdeveloper.reminders.SystemNotification
import com.sunrisekcdeveloper.reminders.WaterPlantReminder
import com.sunrisekcdeveloper.reminders.ForgotToWaterReminder
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
    private val plantCache by lazy { LocalDatabasePlantCache(db.plantDao()) }
    private val notificationCache by lazy { LocalDatabaseNotificationCache(db.notificationDao()) }

    override fun onCreate() {
        super.onCreate()

        globalServices = GlobalServices.builder()
            .add(plantCache)
            .rebind<PlantCache>(plantCache)
            .add(notificationCache)
            .rebind<NotificationCache>(notificationCache)
            .build()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(
                WaterPlantReminder.Factory(
                    plantCache = plantCache,
                    notificationCache = notificationCache,
                    systemNotification = systemNotification,
                    db.notificationDao2(),
                    clock = defaultClock,
                )
            )
            .setWorkerFactory(
                ForgotToWaterReminder.Factory(
                    plantCache = plantCache,
                    notificationCache = notificationCache,
                    systemNotification = systemNotification,
                    clock = defaultClock,
                )
            )
            .build()
}

