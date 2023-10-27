package com.sunrisekcdeveloper.pureplanting.workers

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.PlantNotificationType
import com.sunrisekcdeveloper.shared_test.MutableClock
import com.sunrisekcdeveloper.shared_test.NotificationCacheFake
import com.sunrisekcdeveloper.shared_test.PlantCacheFake
import com.sunrisekcdeveloper.shared_test.plantThatNeedsWaterSoon
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.days
import kotlin.time.toJavaDuration

class ForgotToWaterWorkerTest {

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
        plantCacheFake.throwException = false
        notificationCacheFake.throwException = false
        plantCacheFake.resetData()
        notificationCacheFake.resetData()
    }

    @Test
    fun did_not_forget_to_water_plant_then_no_new_notification_is_created() {
        // SETUP
        val plant = plantThatNeedsWaterSoon(LocalDateTime.now(mutableClock))
        val worker = TestListenableWorkerBuilder<ForgotToWaterWorker>(context)
            .setWorkerFactory(ForgotToWaterWorker.Factory(plantCacheFake, notificationCacheFake, mutableClock))
            .setInputData(Data.Builder().putString(ForgotToWaterWorker.INPUT_PARAM_PLANT_ID, plant.id.toString()).build())
            .build()

        plantCacheFake.save(plant)

        // ACTION & ASSERTIONS
        runBlocking {
            val result = worker.doWork()

            ViewMatchers.assertThat(result, CoreMatchers.`is`(ListenableWorker.Result.success()))
            ViewMatchers.assertThat(notificationCacheFake.all().size, CoreMatchers.`is`(0))
        }
    }

    @Test
    fun forgot_to_water_plant_then_a_single_new_notification_is_created() {
        // SETUP
        val plant = plantThatNeedsWaterSoon(LocalDateTime.now(mutableClock))
        val worker = TestListenableWorkerBuilder<ForgotToWaterWorker>(context)
            .setWorkerFactory(ForgotToWaterWorker.Factory(plantCacheFake, notificationCacheFake, mutableClock))
            .setInputData(Data.Builder().putString(ForgotToWaterWorker.INPUT_PARAM_PLANT_ID, plant.id.toString()).build())
            .build()

        plantCacheFake.save(plant)
        mutableClock.advanceTimeBy(2.days.toJavaDuration())

        // ACTION & ASSERTIONS
        runBlocking {
            val result = worker.doWork()

            ViewMatchers.assertThat(result, CoreMatchers.`is`(ListenableWorker.Result.success()))
            ViewMatchers.assertThat(notificationCacheFake.all().size, CoreMatchers.`is`(1))
            ViewMatchers.assertThat(notificationCacheFake.all().first().type, CoreMatchers.`is`(PlantNotificationType.FORGOT_TO_WATER))
            ViewMatchers.assertThat(notificationCacheFake.all().first().seen, CoreMatchers.`is`(false))
            ViewMatchers.assertThat(notificationCacheFake.all().first().created, CoreMatchers.`is`(Matchers.lessThan(LocalDateTime.now())))
        }
    }

    @Test
    fun exception_encountered_returns_failure() {
        // SETUP
        notificationCacheFake.throwException = true
        val plant = plantThatNeedsWaterSoon(LocalDateTime.now(mutableClock))
        val worker = TestListenableWorkerBuilder<ForgotToWaterWorker>(context)
            .setWorkerFactory(ForgotToWaterWorker.Factory(plantCacheFake, notificationCacheFake, mutableClock))
            .setInputData(Data.Builder().putString(ForgotToWaterWorker.INPUT_PARAM_PLANT_ID, plant.id.toString()).build())
            .build()

        plantCacheFake.save(plant)
        mutableClock.advanceTimeBy(2.days.toJavaDuration())

        // ACTION & ASSERTIONS
        runBlocking {
            val result = worker.doWork()

            notificationCacheFake.throwException = false
            ViewMatchers.assertThat(result, CoreMatchers.`is`(ListenableWorker.Result.failure()))
            ViewMatchers.assertThat(notificationCacheFake.all().size, CoreMatchers.`is`(0))
        }
    }

}