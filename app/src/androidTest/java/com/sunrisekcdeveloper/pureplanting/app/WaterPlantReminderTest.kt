package com.sunrisekcdeveloper.pureplanting.app

import android.content.Context
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.sunrisekcdeveloper.database.PurePlantingDatabase
import com.sunrisekcdeveloper.pureplanting.business.notification.NotificationRepository
import com.sunrisekcdeveloper.pureplanting.business.notification.PlantNotificationType
import com.sunrisekcdeveloper.pureplanting.business.plant.PlantRepository
import com.sunrisekcdeveloper.pureplanting.app.workers.SystemNotification
import com.sunrisekcdeveloper.pureplanting.app.workers.WaterPlantReminder
import com.sunrisekcdeveloper.shared_test.MutableClock
import com.sunrisekcdeveloper.shared_test.now
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Clock

class WaterPlantReminderTest {

    private lateinit var context: Context
    private lateinit var plantRepositoryFake: PlantRepository.Fake
    private lateinit var notificationRepositoryFake: NotificationRepository.Fake
    private lateinit var systemNotification: SystemNotification
    private lateinit var mutableClock: MutableClock
    private lateinit var plantReminderWorkerFactory: WaterPlantReminder.Factory
    private lateinit var db: PurePlantingDatabase

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = inMemoryDatabaseBuilder(context, PurePlantingDatabase::class.java).build()
        plantRepositoryFake = PlantRepository.Fake()
        notificationRepositoryFake = NotificationRepository.Fake()
        mutableClock = MutableClock(Clock.systemDefaultZone())
        systemNotification = SystemNotification(context, plantRepositoryFake)
        plantReminderWorkerFactory = WaterPlantReminder.Factory(plantRepositoryFake, notificationRepositoryFake, systemNotification, db.waterWorkerDao(), mutableClock)
    }

    @After
    fun teardown() {
        plantRepositoryFake.throwException = false
        notificationRepositoryFake.throwException = false
        plantRepositoryFake.resetData()
        notificationRepositoryFake.resetData()
    }

    @Test
    fun with_no_plants_existing_then_no_new_notification_is_created() = runTest {
        // SETUP
        val worker = TestListenableWorkerBuilder<WaterPlantReminder>(context)
            .setWorkerFactory(plantReminderWorkerFactory)
            .build()

        // ACTION & ASSERTIONS
        runBlocking {
            val result = worker.doWork()

            assertThat(result, `is`(ListenableWorker.Result.success()))
            assertThat(notificationRepositoryFake.all().size, `is`(0))
        }
    }

    // todo update to only grab plants in a window of time
    @Test
    fun create_notification_for_all_plants_that_that_need_water_up_to_15min_in_the_future_when_there_are_multiple_that_need_water() = runTest {
        // SETUP
        val worker = TestListenableWorkerBuilder<WaterPlantReminder>(context)
            .setWorkerFactory(plantReminderWorkerFactory)
            .build()

        // ACTION
        plantRepositoryFake.save(plantNeedsWaterNow())
        plantRepositoryFake.save(plantNeedsWaterNow(now().plusMinutes(10)).water())
        plantRepositoryFake.save(plantNeedsWaterNow(now().plusMinutes(30)))
        plantRepositoryFake.save(plantNeedsWaterNow(now().plusMinutes(20)))
        plantRepositoryFake.save(plantNeedsWaterNow(now().minusHours(1)))

        // ASSERTIONS
        runBlocking {
            val result = worker.doWork()
            val notifications = notificationRepositoryFake.all()

            assertThat(result, `is`(ListenableWorker.Result.success()))
            assertThat(notifications.size, `is`(1))
            assertThat(notifications.first().seen, `is`(false))
            assertThat(notifications.first().type, instanceOf(PlantNotificationType.NeedsWater::class.java))
            assertThat(notifications.first().type.targetPlants.size, `is`(2))
        }
    }

    // todo update to only grab plants in a window of time
    @Test
    fun only_create_notification_for_plants_that_have_not_yet_been_watered_today() = runTest {
        // SETUP
        val worker = TestListenableWorkerBuilder<WaterPlantReminder>(context)
            .setWorkerFactory(plantReminderWorkerFactory)
            .build()

        // ACTION
        plantRepositoryFake.save(plantNeedsWaterNow())
        plantRepositoryFake.save(plantNeedsWaterNow(now().plusMinutes(10)))
        plantRepositoryFake.save(plantNeedsWaterNow(now().plusMinutes(30)))
        plantRepositoryFake.save(plantNeedsWaterNow(now().plusMinutes(20)))
        plantRepositoryFake.save(plantNeedsWaterNow(now().minusHours(1)))

        // ASSERTIONS
        runBlocking {
            val result = worker.doWork()
            val notifications = notificationRepositoryFake.all()

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
        plantRepositoryFake.throwException = true
        val worker = TestListenableWorkerBuilder<WaterPlantReminder>(context)
            .setWorkerFactory(
                WaterPlantReminder.Factory(
                    plantRepositoryFake,
                    notificationRepositoryFake,
                    systemNotification,
                    db.waterWorkerDao(),
                    mutableClock
                )
            )
            .build()

        // ACTION & ASSERTIONS
        runBlocking {
            val result = worker.doWork()

            plantRepositoryFake.throwException = false
            assertThat(result, `is`(ListenableWorker.Result.failure()))
            assertThat(notificationRepositoryFake.all().size, `is`(0))
        }
    }
}