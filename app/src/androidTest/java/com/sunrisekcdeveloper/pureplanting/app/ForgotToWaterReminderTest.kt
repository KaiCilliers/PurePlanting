package com.sunrisekcdeveloper.pureplanting.app

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.sunrisekcdeveloper.notification.NotificationCacheFake
import com.sunrisekcdeveloper.plant.PlantCacheFake
import com.sunrisekcdeveloper.reminders.ForgotToWaterReminder
import com.sunrisekcdeveloper.reminders.SystemNotification
import com.sunrisekcdeveloper.shared_test.MutableClock
import com.sunrisekcdeveloper.shared_test.now
import com.sunrisekcdeveloper.shared_test.plantForgotten
import com.sunrisekcdeveloper.shared_test.plantNeedsWaterNow
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Clock

class ForgotToWaterReminderTest {

    private lateinit var context: Context
    private lateinit var plantCacheFake: PlantCacheFake
    private lateinit var notificationsCacheFake: NotificationCacheFake
    private lateinit var systemNotification: SystemNotification
    private lateinit var mutableClock: MutableClock
    private lateinit var workerFactory: ForgotToWaterReminder.Factory

    @Before
    fun setup() {
        plantCacheFake = PlantCacheFake()
        notificationsCacheFake = NotificationCacheFake()
        mutableClock = MutableClock(Clock.systemDefaultZone())
        context = ApplicationProvider.getApplicationContext()
        systemNotification = SystemNotification(context)
        workerFactory = ForgotToWaterReminder.Factory(plantCacheFake, notificationsCacheFake, systemNotification, mutableClock)
    }

    @After
    fun teardown() {
        plantCacheFake.throwException = false
        notificationsCacheFake.throwException = false
        plantCacheFake.resetData()
        notificationsCacheFake.resetData()
    }

    @Test
    fun with_no_plants_existing_do_not_create_a_notification() {
        // SETUP
        val worker = TestListenableWorkerBuilder<ForgotToWaterReminder>(context)
            .setWorkerFactory(workerFactory)
            .build()


        runBlocking {
            // ACTION
            val result = worker.doWork()

            // ASSERTIONS
            assertThat(result, `is`(ListenableWorker.Result.success()))
            assertThat(notificationsCacheFake.all().size, `is`(0))
        }
    }


    @Test
    fun if_there_are_plants_that_needed_water_yesterday_and_they_were_not_watered_then_create_a_notification() = runBlocking {
        // SETUP
        val worker = TestListenableWorkerBuilder<ForgotToWaterReminder>(context)
            .setWorkerFactory(workerFactory)
            .build()

        plantCacheFake.save(plantForgotten(now()))
        plantCacheFake.save(plantForgotten(now()))


        runBlocking {
            // ACTION
            val result = worker.doWork()
            val notifications = notificationsCacheFake.all()

            // ASSERTIONS
            assertThat(result, `is`(ListenableWorker.Result.success()))
            assertThat(notifications.size, `is`(1))
            assertThat(notifications.first().type, instanceOf(com.sunrisekcdeveloper.notification.PlantNotificationType.ForgotToWater::class.java))
            assertThat(notifications.first().type.targetPlants.size, `is`(2))
            assertThat(notifications.first().seen, `is`(false))
        }
    }

    @Test
    fun if_there_are_plants_but_none_were_forgotten_to_water_then_do_not_create_a_notification() = runBlocking {
        // SETUP
        val worker = TestListenableWorkerBuilder<ForgotToWaterReminder>(context)
            .setWorkerFactory(workerFactory)
            .build()

        plantCacheFake.save(plantNeedsWaterNow())
        plantCacheFake.save(plantForgotten(now()).water())


        runBlocking {
            // ACTION
            val result = worker.doWork()
            val notifications = notificationsCacheFake.all()

            // ASSERTIONS
            assertThat(result, `is`(ListenableWorker.Result.success()))
            assertThat(notifications.size, `is`(0))
        }
    }

    @Test
    fun exception_encountered_returns_retry() {
        // SETUP
        plantCacheFake.throwException = true
        val worker = TestListenableWorkerBuilder<ForgotToWaterReminder>(context)
            .setWorkerFactory(workerFactory)
            .build()

        // ACTION & ASSERTIONS
        runBlocking {
            val result = worker.doWork()

            plantCacheFake.throwException = false
            assertThat(result, `is`(ListenableWorker.Result.retry()))
            assertThat(notificationsCacheFake.all().size, `is`(0))
        }
    }

}