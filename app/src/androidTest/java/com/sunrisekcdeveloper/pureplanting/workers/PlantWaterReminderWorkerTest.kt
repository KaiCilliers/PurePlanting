package com.sunrisekcdeveloper.pureplanting.workers

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.PlantNotificationType
import com.sunrisekcdeveloper.pureplanting.util.SystemNotification
import com.sunrisekcdeveloper.pureplanting.workers.DailyPlantReminderWorker.Factory
import com.sunrisekcdeveloper.shared_test.MutableClock
import com.sunrisekcdeveloper.shared_test.NotificationsCacheFake
import com.sunrisekcdeveloper.shared_test.PlantCacheFake
import com.sunrisekcdeveloper.shared_test.now
import com.sunrisekcdeveloper.shared_test.plantNeedsWaterNow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Clock

class PlantWaterReminderWorkerTest {

    private lateinit var context: Context
    private lateinit var plantCacheFake: PlantCacheFake
    private lateinit var notificationsCacheFake: NotificationsCacheFake
    private lateinit var systemNotification: SystemNotification
    private lateinit var mutableClock: MutableClock
    private lateinit var plantReminderWorkerFactory: Factory

    @Before
    fun setup() {
        plantCacheFake = PlantCacheFake()
        notificationsCacheFake = NotificationsCacheFake()
        mutableClock = MutableClock(Clock.systemDefaultZone())
        context = ApplicationProvider.getApplicationContext()
        systemNotification = SystemNotification(context)
        plantReminderWorkerFactory = Factory(plantCacheFake, notificationsCacheFake, systemNotification, mutableClock)
    }

    @After
    fun teardown() {
        plantCacheFake.throwException = false
        notificationsCacheFake.throwException = false
        plantCacheFake.resetData()
        notificationsCacheFake.resetData()
    }

    @Test
    fun with_no_plants_existing_then_no_new_notification_is_created() = runTest {
        // SETUP
        val worker = TestListenableWorkerBuilder<DailyPlantReminderWorker>(context)
            .setWorkerFactory(plantReminderWorkerFactory)
            .build()

        // ACTION & ASSERTIONS
        runBlocking {
            val result = worker.doWork()

            assertThat(result, `is`(ListenableWorker.Result.success()))
            assertThat(notificationsCacheFake.all().size, `is`(0))
        }
    }

    @Test
    fun create_notification_for_all_plants_that_that_need_water_up_to_15min_in_the_future_when_there_are_multiple_that_need_water() = runTest {
        // SETUP
        val worker = TestListenableWorkerBuilder<DailyPlantReminderWorker>(context)
            .setWorkerFactory(plantReminderWorkerFactory)
            .build()

        // ACTION
        plantCacheFake.save(plantNeedsWaterNow())
        plantCacheFake.save(plantNeedsWaterNow(now().plusMinutes(10)).water())
        plantCacheFake.save(plantNeedsWaterNow(now().plusMinutes(30)))
        plantCacheFake.save(plantNeedsWaterNow(now().plusMinutes(20)))
        plantCacheFake.save(plantNeedsWaterNow(now().minusHours(1)))

        // ASSERTIONS
        runBlocking {
            val result = worker.doWork()
            val notifications = notificationsCacheFake.all()

            assertThat(result, `is`(ListenableWorker.Result.success()))
            assertThat(notifications.size, `is`(1))
            assertThat(notifications.first().seen, `is`(false))
            assertThat(notifications.first().type, instanceOf(PlantNotificationType.NeedsWater::class.java))
            assertThat(notifications.first().type.targetPlants.size, `is`(2))
        }
    }

    @Test
    fun only_create_notification_for_plants_that_have_not_yet_been_watered_today() = runTest {
        // SETUP
        val worker = TestListenableWorkerBuilder<DailyPlantReminderWorker>(context)
            .setWorkerFactory(plantReminderWorkerFactory)
            .build()

        // ACTION
        plantCacheFake.save(plantNeedsWaterNow())
        plantCacheFake.save(plantNeedsWaterNow(now().plusMinutes(10)))
        plantCacheFake.save(plantNeedsWaterNow(now().plusMinutes(30)))
        plantCacheFake.save(plantNeedsWaterNow(now().plusMinutes(20)))
        plantCacheFake.save(plantNeedsWaterNow(now().minusHours(1)))

        // ASSERTIONS
        runBlocking {
            val result = worker.doWork()
            val notifications = notificationsCacheFake.all()

            assertThat(result, `is`(ListenableWorker.Result.success()))
            assertThat(notifications.size, `is`(1))
            assertThat(notifications.first().seen, `is`(false))
            assertThat(notifications.first().type, instanceOf(PlantNotificationType.NeedsWater::class.java))
            assertThat(notifications.first().type.targetPlants.size, `is`(3))
        }
    }

    @Test
    fun exception_encountered_returns_failure() = runTest {
        // SETUP
        plantCacheFake.throwException = true
        val worker = TestListenableWorkerBuilder<DailyPlantReminderWorker>(context)
            .setWorkerFactory(Factory(plantCacheFake, notificationsCacheFake, systemNotification, mutableClock))
            .build()

        // ACTION & ASSERTIONS
        runBlocking {
            val result = worker.doWork()

            plantCacheFake.throwException = false
            assertThat(result, `is`(ListenableWorker.Result.failure()))
            assertThat(notificationsCacheFake.all().size, `is`(0))
        }
    }
}