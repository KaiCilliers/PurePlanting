package com.sunrisekcdeveloper.pureplanting.app

import android.app.Application
import androidx.work.Configuration
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationDomain
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationsCache
import com.sunrisekcdeveloper.pureplanting.features.component.plants.InMemoryPlantCache
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantCache
import com.sunrisekcdeveloper.pureplanting.util.SystemNotification
import com.sunrisekcdeveloper.pureplanting.workers.DailyPlantReminderWorker
import com.sunrisekcdeveloper.pureplanting.workers.ForgotToWaterWorker
import com.zhuinden.simplestack.GlobalServices
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.rebind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
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
            // add dependencies here
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

// todo relocate
class InMemoryNotificationCache : NotificationsCache {

    private val notifications = MutableStateFlow<List<NotificationDomain>>(emptyList())

    override suspend fun save(notification: NotificationDomain) {
        notifications.update {
            it.toMutableList().apply {
                val existingIndex = this.map { it.id }.indexOf(notification.id)
                if (existingIndex >= 0) this[existingIndex] = notification
                else add(notification)
            }
        }
    }

    override suspend fun all(): List<NotificationDomain> {
        return notifications.value
    }

    override suspend fun markAsSeen(id: String) {
        notifications.update {
            it.toMutableList().apply {
                val existingItem = this.first { it.id == id }
                val existingIndex = this.map { it.id }.indexOf(id)
                if (existingIndex >= 0) this[existingIndex] = existingItem.copy(seen = true)
            }
        }
    }

    override fun observe(): Flow<List<NotificationDomain>> = notifications
}