package com.sunrisekcdeveloper.pureplanting.workers

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.PlantNotificationType
import com.sunrisekcdeveloper.pureplanting.workers.DailyPlantReminderWorker.DailyPlantReminderWorkerFactory
import com.sunrisekcdeveloper.shared_test.MutableClock
import com.sunrisekcdeveloper.shared_test.NotificationCacheFake
import com.sunrisekcdeveloper.shared_test.PlantCacheFake
import com.sunrisekcdeveloper.shared_test.plantThatNeedsWaterSoon
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.LocalDateTime

class DailyPlantReminderWorkerTest {

    private lateinit var context: Context
    private lateinit var plantCacheFake: PlantCacheFake
    private lateinit var notificationCacheFake: NotificationCacheFake
    private lateinit var mutableClock: MutableClock

    @Before
    fun setup() {
        plantCacheFake = PlantCacheFake()
        notificationCacheFake = NotificationCacheFake()
        mutableClock = MutableClock(Clock.systemDefaultZone())
        context = ApplicationProvider.getApplicationContext()
    }

    @After
    fun teardown() {
        plantCacheFake.resetData()
        notificationCacheFake.resetData()
    }

    @Test
    fun no_plants_to_water_soon_then_no_new_notification_is_created() {
        // SETUP
        val worker = TestListenableWorkerBuilder<DailyPlantReminderWorker>(context)
            .setWorkerFactory(DailyPlantReminderWorkerFactory(plantCacheFake, notificationCacheFake, mutableClock))
            .build()

        // ACTION & ASSERTIONS
        runBlocking {
            val result = worker.doWork()

            assertThat(result, `is`(ListenableWorker.Result.success()))
            assertThat(notificationCacheFake.all().size, `is`(0))
        }
    }

    @Test
    fun there_are_plants_that_need_to_be_watered_soon_then_a_single_new_notification_is_created() {
        // SETUP
        val worker = TestListenableWorkerBuilder<DailyPlantReminderWorker>(context)
            .setWorkerFactory(DailyPlantReminderWorkerFactory(plantCacheFake, notificationCacheFake, mutableClock))
            .build()

        plantCacheFake.save(plantThatNeedsWaterSoon(LocalDateTime.now(mutableClock)))
        // with offset, it does not need to be watered soon
        plantCacheFake.save(plantThatNeedsWaterSoon(LocalDateTime.now(mutableClock), offsetFutureDays = 2))
        plantCacheFake.save(plantThatNeedsWaterSoon(LocalDateTime.now(mutableClock)))

        // ACTION & ASSERTIONS
        runBlocking {
            val result = worker.doWork()

            assertThat(result, `is`(ListenableWorker.Result.success()))
            assertThat(notificationCacheFake.all().size, `is`(1))
            assertThat(notificationCacheFake.all().first().type, `is`(PlantNotificationType.WATER_SOON))
            assertThat(notificationCacheFake.all().first().seen, `is`(false))
            assertThat(notificationCacheFake.all().first().created, `is`(Matchers.lessThan(LocalDateTime.now())))
        }
    }
}