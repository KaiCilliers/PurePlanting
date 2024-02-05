package com.sunrisekcdeveloper.pureplanting

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.sunrisekcdeveloper.database.PurePlantingDatabase
import com.sunrisekcdeveloper.design.ui.SnackbarEmitter
import com.sunrisekcdeveloper.notification.NotificationRepository
import com.sunrisekcdeveloper.plant.PlantRepository
import com.sunrisekcdeveloper.alarm.models.AlarmInfo
import com.sunrisekcdeveloper.alarm.AlarmInfoRepository
import com.sunrisekcdeveloper.alarm.AlarmScheduler
import com.sunrisekcdeveloper.alarm.models.AlarmType
import com.sunrisekcdeveloper.pureplanting.workers.CompositeWorkerFactory
import com.sunrisekcdeveloper.pureplanting.workers.ForgotToWaterReminder
import com.sunrisekcdeveloper.pureplanting.workers.SystemNotification
import com.sunrisekcdeveloper.pureplanting.workers.WaterPlantReminder
import com.zhuinden.simplestack.GlobalServices
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.rebind
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDateTime
import java.time.LocalTime

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
    private val alarmInfoRepo by lazy { AlarmInfoRepository.Default(db.alarmInfoDao()) }
    private val alarmScheduler by lazy { AlarmScheduler.Default(applicationContext, alarmInfoRepo) }

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

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(CompositeWorkerFactory(forgotFactory, waterFactory))
            .build()

    override fun onCreate() {
        super.onCreate()

        globalServices = GlobalServices.builder()
            .add(plantRepository)
            .rebind<PlantRepository>(plantRepository)
            .add(notificationRepository)
            .rebind<NotificationRepository>(notificationRepository)
            .add(snackbarEmitter)
            .add(alarmScheduler)
            .rebind<AlarmScheduler>(alarmScheduler)
            .build()

        MainScope().launch {
            val midnight = LocalDateTime.of(LocalDateTime.now().toLocalDate().plusDays(1), LocalTime.MIDNIGHT)
            alarmScheduler.schedule(
                AlarmInfo(time = midnight, type = AlarmType.ForgotToWater())
            )
        }
    }
}

