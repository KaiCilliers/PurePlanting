package com.sunrisekcdeveloper.pureplanting.app

import android.app.Application
import androidx.work.Configuration
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.InMemoryNotificationCache
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationsCache
import com.sunrisekcdeveloper.pureplanting.features.component.plants.InMemoryPlantCache
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantCache
import com.sunrisekcdeveloper.pureplanting.util.SystemNotification
import com.sunrisekcdeveloper.pureplanting.workers.DailyPlantReminderWorker
import com.sunrisekcdeveloper.pureplanting.workers.ForgotToWaterWorker
import com.zhuinden.simplestack.GlobalServices
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.rebind
import java.time.Clock

class PurePlantingApplication : Application(), Configuration.Provider {
    lateinit var globalServices: GlobalServices
        private set

    // Create global dependencies
    private val inMemoryPlantCache = InMemoryPlantCache()
    private val inMemoryNotificationsCache = InMemoryNotificationCache()
    private val systemNotification by lazy { SystemNotification(applicationContext) }
    private val defaultClock = Clock.systemDefaultZone()

    override fun onCreate() {
        super.onCreate()

        globalServices = GlobalServices.builder()
            .add(inMemoryPlantCache)
            .rebind<PlantCache>(inMemoryPlantCache)
            .add(inMemoryNotificationsCache)
            .rebind<NotificationsCache>(inMemoryNotificationsCache)
            .build()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(
                DailyPlantReminderWorker.Factory(
                    plantCache = inMemoryPlantCache,
                    notificationsCache = inMemoryNotificationsCache,
                    systemNotification = systemNotification,
                    clock = defaultClock,
                )
            )
            .setWorkerFactory(
                ForgotToWaterWorker.Factory(
                    plantCache = inMemoryPlantCache,
                    notificationsCache = inMemoryNotificationsCache,
                    systemNotification = systemNotification,
                    clock = defaultClock,
                )
            )
            .build()
}

