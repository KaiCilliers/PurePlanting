package com.sunrisekcdeveloper.pureplanting.app

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.sunrisekcdeveloper.pureplanting.MutableClock
import com.sunrisekcdeveloper.pureplanting.app.workers.ForgotToWaterReminder
import com.sunrisekcdeveloper.pureplanting.app.workers.SystemNotification
import com.sunrisekcdeveloper.pureplanting.business.notification.NotificationRepository
import com.sunrisekcdeveloper.pureplanting.business.notification.PlantNotificationType
import com.sunrisekcdeveloper.pureplanting.business.plant.PlantRepository
import com.sunrisekcdeveloper.pureplanting.library.database.PurePlantingDatabase
import com.sunrisekcdeveloper.pureplanting.now
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
    private lateinit var plantRepositoryFake: PlantRepository.Fake
    private lateinit var notificationsRepositoryFake: NotificationRepository.Fake
    private lateinit var systemNotification: SystemNotification
    private lateinit var mutableClock: MutableClock
    private lateinit var workerFactory: ForgotToWaterReminder.Factory
    private lateinit var db: PurePlantingDatabase

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, PurePlantingDatabase::class.java).build()
        plantRepositoryFake = PlantRepository.Fake()
        notificationsRepositoryFake = NotificationRepository.Fake()
        mutableClock = MutableClock(Clock.systemDefaultZone())
        systemNotification = SystemNotification(context, plantRepositoryFake)
        workerFactory = ForgotToWaterReminder.Factory(plantRepositoryFake, notificationsRepositoryFake, systemNotification, db.forgotWaterWorkerDao(), mutableClock)
    }

    @After
    fun teardown() {
        plantRepositoryFake.throwException = false
        notificationsRepositoryFake.throwException = false
        plantRepositoryFake.resetData()
        notificationsRepositoryFake.resetData()
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
            assertThat(notificationsRepositoryFake.all().size, `is`(0))
        }
    }


    @Test
    fun if_there_are_plants_that_needed_water_yesterday_and_they_were_not_watered_then_create_a_notification() = runBlocking {
        // SETUP
        val worker = TestListenableWorkerBuilder<ForgotToWaterReminder>(context)
            .setWorkerFactory(workerFactory)
            .build()

        plantRepositoryFake.save(plantForgotten(now()))
        plantRepositoryFake.save(plantForgotten(now()))


        runBlocking {
            // ACTION
            val result = worker.doWork()
            val notifications = notificationsRepositoryFake.all()

            // ASSERTIONS
            assertThat(result, `is`(ListenableWorker.Result.success()))
            assertThat(notifications.size, `is`(1))
            assertThat(notifications.first().type, instanceOf(PlantNotificationType.ForgotToWater::class.java))
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

        plantRepositoryFake.save(plantNeedsWaterNow())
        plantRepositoryFake.save(plantForgotten(now()).water())


        runBlocking {
            // ACTION
            val result = worker.doWork()
            val notifications = notificationsRepositoryFake.all()

            // ASSERTIONS
            assertThat(result, `is`(ListenableWorker.Result.success()))
            assertThat(notifications.size, `is`(0))
        }
    }

    @Test
    fun exception_encountered_returns_failure() {
        // SETUP
        plantRepositoryFake.throwException = true
        val worker = TestListenableWorkerBuilder<ForgotToWaterReminder>(context)
            .setWorkerFactory(workerFactory)
            .build()

        // ACTION & ASSERTIONS
        runBlocking {
            val result = worker.doWork()

            plantRepositoryFake.throwException = false
            assertThat(result, `is`(ListenableWorker.Result.failure()))
            assertThat(notificationsRepositoryFake.all().size, `is`(0))
        }
    }

}