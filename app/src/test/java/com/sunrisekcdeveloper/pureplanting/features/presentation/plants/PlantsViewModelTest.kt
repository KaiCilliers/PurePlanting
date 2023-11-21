package com.sunrisekcdeveloper.pureplanting.features.presentation.plants

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.sunrisekcdeveloper.shared_test.MutableClock
import com.sunrisekcdeveloper.shared_test.NotificationsCacheFake
import com.sunrisekcdeveloper.shared_test.PlantCacheFake
import com.sunrisekcdeveloper.shared_test.now
import com.sunrisekcdeveloper.shared_test.plant
import com.sunrisekcdeveloper.shared_test.plantForgotten
import com.sunrisekcdeveloper.shared_test.plantNeedsWater
import com.sunrisekcdeveloper.shared_test.plantNeedsWaterNow
import com.sunrisekcdeveloper.shared_test.today
import com.sunrisekcdeveloper.shared_test.unreadNotification
import com.zhuinden.simplestack.Backstack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.toJavaDuration

@OptIn(ExperimentalCoroutinesApi::class)
class PlantsViewModelTest {

    private lateinit var plantCacheFake: PlantCacheFake
    private lateinit var viewModel: PlantsViewModel
    private lateinit var mutableClock: MutableClock
    private lateinit var notificationsCacheFake: NotificationsCacheFake

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        mutableClock = MutableClock(Clock.systemDefaultZone())
        plantCacheFake = PlantCacheFake()
        notificationsCacheFake = NotificationsCacheFake()
        viewModel = PlantsViewModel(plantCacheFake, notificationsCacheFake, mutableClock, Backstack())
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        plantCacheFake.resetData()
    }

    @Test
    fun `upon init, upcoming filter is selected`() = runTest {
        // SETUP
        val filterFlow = viewModel.activeFilter

        // ACTION & ASSERTIONS
        filterFlow.test {
            val emission = awaitItem()

            assertThat(emission).isEqualTo(PlantListFilter.UPCOMING)
        }
    }

    @Test
    fun `upcoming filter selected, display only plants that need water`() = runTest {
        // SETUP
        val plantsFlow = viewModel.plants
        val today = today()

        plantCacheFake.save(plantNeedsWaterNow(today))
        plantCacheFake.save(plantNeedsWaterNow(today))
        plantCacheFake.save(plantNeedsWater(today.minusDays(3)))

        // ACTION
        viewModel.setFilter(PlantListFilter.UPCOMING)

        // ASSERTIONS
        plantsFlow.test {
            awaitItem()
            val emission = awaitItem()

            assertThat(emission).hasSize(2)
        }
    }

    @Test
    fun `forgot to water filter selected, display only plants that have been forgotten to be watered`() = runTest {
        // SETUP
        val plantsFlow = viewModel.plants
        val today = today()

        plantCacheFake.save(plantForgotten())
        plantCacheFake.save(plantForgotten())
        plantCacheFake.save(plantNeedsWaterNow(today))
        plantCacheFake.save(plantForgotten())

        // ACTION
        viewModel.setFilter(PlantListFilter.FORGOT_TO_WATER)

        // ASSERTIONS
        plantsFlow.test {
            awaitItem()
            val emission = awaitItem()

            assertThat(emission).hasSize(3)
        }
    }

    @Test
    fun `history filter selected, display only plants that have a record of being watered`() = runTest {
        // SETUP
        val plantsFlow = viewModel.plants

        plantCacheFake.save(plant())
        plantCacheFake.save(plant().water())
        plantCacheFake.save(plant().water().water())
        plantCacheFake.save(plant())
        plantCacheFake.save(plant().water())

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()
            viewModel.setFilter(PlantListFilter.HISTORY)

            val emission = awaitItem()

            assertThat(emission).hasSize(3)
        }
    }

    @Test
    fun `water a plant in upcoming list removes it from the list`() = runTest {
        // SETUP
        val plantsFlow = viewModel.plants
        val plantToWater = plantNeedsWaterNow()

        plantCacheFake.save(plantNeedsWaterNow())
        plantCacheFake.save(plant().water())
        plantCacheFake.save(plant().water().water())
        plantCacheFake.save(plantToWater)
        plantCacheFake.save(plant().water())
        plantCacheFake.save(plantForgotten())

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            viewModel.setFilter(PlantListFilter.UPCOMING)
            assertThat(awaitItem()).hasSize(2)

            viewModel.waterPlant(plantToWater)
            assertThat(awaitItem()).hasSize(1)
        }
    }

    @Test
    fun `undo watering of a plant with single record in history removes item from history list`() = runTest {
        // SETUP
        val plantsFlow = viewModel.plants
        val plantToUndoWatering = plant().water()

        plantCacheFake.save(plantNeedsWaterNow())
        plantCacheFake.save(plant().water())
        plantCacheFake.save(plant().water().water())
        plantCacheFake.save(plantNeedsWaterNow())
        plantCacheFake.save(plantToUndoWatering)
        plantCacheFake.save(plantForgotten())

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            viewModel.setFilter(PlantListFilter.HISTORY)
            assertThat(awaitItem()).hasSize(3)

            viewModel.undoWaterPlant(plantToUndoWatering)
            assertThat(awaitItem()).hasSize(2)
        }
    }

    @Test
    fun `undo watering of a plant with more than one record in history list does not remove it from the list`() = runTest {
        // SETUP

        // ACTION & ASSERTIONS

    }

    @Test
    fun `water a plant in forgot to water list removes it from the list`() = runTest {
        // SETUP
        val plantsFlow = viewModel.plants
        val plantToUndoWatering = plant().water().water()

        plantCacheFake.save(plantNeedsWaterNow())
        plantCacheFake.save(plant().water())
        plantCacheFake.save(plant().water().water())
        plantCacheFake.save(plantNeedsWaterNow())
        plantCacheFake.save(plantToUndoWatering)
        plantCacheFake.save(plantForgotten())

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            viewModel.setFilter(PlantListFilter.HISTORY)
            assertThat(awaitItem()).hasSize(3)

            viewModel.undoWaterPlant(plantToUndoWatering)
            assertThat(awaitItem()).hasSize(3)
        }
    }

    // note test a delete on each filter    , i,e, 3 deletes and 3 assserts
    @Test
    fun `delete a plant removes it from the current list`() = runTest {
        // SETUP
        val plantsFlow = viewModel.plants
        val upcomingPlant = plantNeedsWaterNow()
        val forgottenPlant = plantForgotten()
        val wateredPlant = plant().water().water()

        plantCacheFake.save(upcomingPlant)
        plantCacheFake.save(wateredPlant)
        plantCacheFake.save(forgottenPlant)
        plantCacheFake.save(plant().water().water())
        plantCacheFake.save(plantForgotten())

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            assertThat(awaitItem()).hasSize(1)
            viewModel.removePlant(upcomingPlant)
            assertThat(awaitItem()).hasSize(0)

            viewModel.setFilter(PlantListFilter.FORGOT_TO_WATER)
            assertThat(awaitItem()).hasSize(2)
            viewModel.removePlant(forgottenPlant)
            assertThat(awaitItem()).hasSize(1)

            viewModel.setFilter(PlantListFilter.HISTORY)
            assertThat(awaitItem()).hasSize(2)
            viewModel.removePlant(wateredPlant)
            assertThat(awaitItem()).hasSize(1)
        }
    }

    // note test a delete on each filter    , i,e, 3 deletes and 3 assserts
    @Test
    fun `undo deleting a plant returns it to the list it was deleted from`() = runTest {
        // SETUP
        val plantsFlow = viewModel.plants
        val upcomingPlant = plantNeedsWaterNow()
        val forgottenPlant = plantForgotten()
        val wateredPlant = plant().water().water()

        plantCacheFake.save(upcomingPlant)
        plantCacheFake.save(wateredPlant)
        plantCacheFake.save(forgottenPlant)
        plantCacheFake.save(plant().water().water())
        plantCacheFake.save(plantForgotten())

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            assertThat(awaitItem()).hasSize(1)
            viewModel.removePlant(upcomingPlant)
            assertThat(awaitItem()).hasSize(0)
            viewModel.undoRemove(upcomingPlant.id)
            assertThat(awaitItem()).hasSize(1)

            viewModel.setFilter(PlantListFilter.FORGOT_TO_WATER)
            assertThat(awaitItem()).hasSize(2)
            viewModel.removePlant(forgottenPlant)
            assertThat(awaitItem()).hasSize(1)
            viewModel.undoRemove(forgottenPlant.id)
            assertThat(awaitItem()).hasSize(2)

            viewModel.setFilter(PlantListFilter.HISTORY)
            assertThat(awaitItem()).hasSize(2)
            viewModel.removePlant(wateredPlant)
            assertThat(awaitItem()).hasSize(1)
            viewModel.undoRemove(wateredPlant.id)
            assertThat(awaitItem()).hasSize(2)
        }
    }

    @Test
    fun `upcoming plants are sorted in ascending order by the time it needs water`() = runTest {
        // SETUP
        val plantsFlow = viewModel.plants
        val today = today()
        val plantMorning = plantNeedsWaterNow(today.withHour(8))
        val plantAfternoon = plantNeedsWaterNow(today.withHour(12))
        val plantEvening = plantNeedsWaterNow(today.withHour(17))

        plantCacheFake.save(plantAfternoon)
        plantCacheFake.save(plantEvening)
        plantCacheFake.save(plantMorning)

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            assertThat(awaitItem()).containsExactly(plantMorning, plantAfternoon, plantEvening)
        }
    }

    @Test
    fun `forgot to water plants are sorted in ascending order by the date it needs water`() = runTest {
        // SETUP
        val plantsFlow = viewModel.plants
        val today = today()
        val plantMorning = plantForgotten(today.withHour(8))
        val plantAfternoon = plantForgotten(today.withHour(12))
        val plantEvening = plantForgotten(today.withHour(17))

        plantCacheFake.save(plantAfternoon)
        plantCacheFake.save(plantMorning)
        plantCacheFake.save(plantEvening)

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            viewModel.setFilter(PlantListFilter.FORGOT_TO_WATER)
            assertThat(awaitItem()).containsExactly(plantMorning, plantAfternoon, plantEvening)
        }
    }

    @Test
    fun `history plants are sorted in descending order by the latest date it was watered`() = runTest {
        // SETUP
        val plantsFlow = viewModel.plants
        val wateredThird = plant()
        val wateredSecond = plant()
        val wateredFirst = plant()

        mutableClock.reverseTimeBy(1.hours.toJavaDuration())
        plantCacheFake.save(wateredThird.water(mutableClock))

        mutableClock.reverseTimeBy(1.hours.toJavaDuration())
        plantCacheFake.save(wateredSecond.water(mutableClock))

        mutableClock.reverseTimeBy(1.hours.toJavaDuration())
        plantCacheFake.save(wateredFirst.water(mutableClock))

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            viewModel.setFilter(PlantListFilter.HISTORY)
            val emission = awaitItem()

            assertThat(emission).hasSize(3)
            assertThat(emission.first().dateLastWatered?.hour).isEqualTo(now().minusHours(1).hour)
            assertThat(emission[1].dateLastWatered?.hour).isEqualTo(now().minusHours(2).hour)
            assertThat(emission[2].dateLastWatered?.hour).isEqualTo(now().minusHours(3).hour)
        }
    }


    @Test
    fun `undo watering of a plant with multiple watering records updates the order of the items in history list`() = runTest {
        // SETUP
        val plantsFlow = viewModel.plants
        var plantToUndo = plant()

        // now is 4pm
        // water at 2pm
        mutableClock.reverseTimeBy(2.hours.toJavaDuration())
        plantCacheFake.save(plant().water(mutableClock))

        // water at 1pm
        mutableClock.reverseTimeBy(1.hours.toJavaDuration())
        plantToUndo = plantToUndo.water(mutableClock)
        plantCacheFake.save(plantToUndo)

        // water at 11am
        mutableClock.reverseTimeBy(2.hours.toJavaDuration())
        plantCacheFake.save(plant().water(mutableClock))

        // water at 3pm
        mutableClock.reset()
        mutableClock.reverseTimeBy(1.hours.toJavaDuration())
        plantToUndo = plantToUndo.water(mutableClock)
        plantCacheFake.save(plantToUndo)

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            viewModel.setFilter(PlantListFilter.HISTORY)

            val emission = awaitItem()
            assertThat(emission).hasSize(3)
            assertThat(emission.first().dateLastWatered?.hour).isEqualTo(now().minusHours(1).hour) // 3pm
            assertThat(emission[1].dateLastWatered?.hour).isEqualTo(now().minusHours(2).hour) // 2pm
            assertThat(emission[2].dateLastWatered?.hour).isEqualTo(now().minusHours(5).hour) // 11am

            viewModel.undoWaterPlant(plantToUndo)

            val emission2 = awaitItem()
            assertThat(emission2.first().dateLastWatered?.hour).isEqualTo(now().minusHours(2).hour) // 2pm
            assertThat(emission2[1].dateLastWatered?.hour).isEqualTo(now().minusHours(3).hour) // 1pm
            assertThat(emission2[2].dateLastWatered?.hour).isEqualTo(now().minusHours(5).hour) // 11am
        }
    }

    @Test
    fun `has unread notifications is true when notifications exist that have not been read`() = runTest {
        // SETUP
        val unreadNotificationsFlow = viewModel.hasUnreadNotifications

        notificationsCacheFake.save(unreadNotification())
        notificationsCacheFake.save(unreadNotification())
        notificationsCacheFake.save(unreadNotification())

        // ACTION & ASSERTIONS
        unreadNotificationsFlow.test {
            awaitItem()

            val emission = awaitItem()
            assertThat(emission).isTrue()
        }
    }

    @Test
    fun `has unread notifications is false after notifications have been read`() = runTest {
        // SETUP
        val unreadNotificationsFlow = viewModel.hasUnreadNotifications
        val notificationOne = unreadNotification()
        val notificationTwo = unreadNotification()

        notificationsCacheFake.save(notificationOne)
        notificationsCacheFake.save(notificationTwo)

        // ACTION & ASSERTIONS
        unreadNotificationsFlow.test {
            awaitItem()

            val emission = awaitItem()
            assertThat(emission).isTrue()

            notificationsCacheFake.save(notificationOne.copy(seen = true))
            notificationsCacheFake.save(notificationTwo.copy(seen = true))

            val emission3 = awaitItem()
            assertThat(emission3).isFalse()
        }
    }
}