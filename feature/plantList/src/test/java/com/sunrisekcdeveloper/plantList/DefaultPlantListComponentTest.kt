package com.sunrisekcdeveloper.pureplanting.features.presentation.plants

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.sunrisekcdeveloper.notification.domain.NotificationRepository
import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import com.sunrisekcdeveloper.plantList.PlantListViewModel
import com.sunrisekcdeveloper.plantList.PlantTabFilter
import com.sunrisekcdeveloper.plantList.plant
import com.sunrisekcdeveloper.plantList.plantForgotten
import com.sunrisekcdeveloper.plantList.plantNeedsWater
import com.sunrisekcdeveloper.plantList.plantNeedsWaterNow
import com.sunrisekcdeveloper.shared_test.MutableClock
import com.sunrisekcdeveloper.shared_test.now
import com.sunrisekcdeveloper.shared_test.today
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
class DefaultPlantListComponentTest {

    private lateinit var plantRepositoryFake: PlantRepository.Fake
    private lateinit var component: PlantListViewModel.Default
    private lateinit var mutableClock: MutableClock
    private lateinit var notificationRepositoryFake: NotificationRepository.Fake
    private lateinit var router: PlantListViewModel.Router

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        mutableClock = MutableClock(Clock.systemDefaultZone())
        plantRepositoryFake = PlantRepository.Fake()
        notificationRepositoryFake = NotificationRepository.Fake()
        router = object : PlantListViewModel.Router {
            override fun goToAddPlant() {}
            override fun goToPlantDetail(plant: Plant) {}
        }
        component = PlantListViewModel.Default(plantRepositoryFake, router, mutableClock)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        plantRepositoryFake.resetData()
    }

    @Test
    fun `upon init, upcoming filter is selected`() = runTest {
        // SETUP
        val filterFlow = component.filter

        // ACTION & ASSERTIONS
        filterFlow.test {
            val emission = awaitItem()

            assertThat(emission).isEqualTo(PlantTabFilter.UPCOMING)
        }
    }

    @Test
    fun `upcoming filter selected, display only plants that need water`() = runTest {
        // SETUP
        val plantsFlow = component.plants
        val today = today()

        plantRepositoryFake.save(plantNeedsWaterNow(today))
        plantRepositoryFake.save(plantNeedsWaterNow(today))
        plantRepositoryFake.save(plantNeedsWater(today.minusDays(3)))

        // ACTION
        component.onFilterChange(PlantTabFilter.UPCOMING)

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
        val plantsFlow = component.plants
        val today = today()

        plantRepositoryFake.save(plantForgotten())
        plantRepositoryFake.save(plantForgotten())
        plantRepositoryFake.save(plantNeedsWaterNow(today))
        plantRepositoryFake.save(plantForgotten())

        // ACTION
        component.onFilterChange(PlantTabFilter.FORGOT_TO_WATER)

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
        val plantsFlow = component.plants

        plantRepositoryFake.save(plant())
        plantRepositoryFake.save(plant().water())
        plantRepositoryFake.save(plant().water().water())
        plantRepositoryFake.save(plant())
        plantRepositoryFake.save(plant().water())

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()
            component.onFilterChange(PlantTabFilter.HISTORY)

            val emission = awaitItem()

            assertThat(emission).hasSize(3)
        }
    }

    @Test
    fun `water a plant in upcoming list removes it from the list`() = runTest {
        // SETUP
        val plantsFlow = component.plants
        val plantToWater = plantNeedsWaterNow()

        plantRepositoryFake.save(plantNeedsWaterNow())
        plantRepositoryFake.save(plant().water())
        plantRepositoryFake.save(plant().water().water())
        plantRepositoryFake.save(plantToWater)
        plantRepositoryFake.save(plant().water())
        plantRepositoryFake.save(plantForgotten())

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            component.onFilterChange(PlantTabFilter.UPCOMING)
            assertThat(awaitItem()).hasSize(2)

            component.onWaterPlant(plantToWater)
            assertThat(awaitItem()).hasSize(1)
        }
    }

    @Test
    fun `undo watering of a plant with single record in history removes item from history list`() = runTest {
        // SETUP
        val plantsFlow = component.plants
        val plantToUndoWatering = plant().water()

        plantRepositoryFake.save(plantNeedsWaterNow())
        plantRepositoryFake.save(plant().water())
        plantRepositoryFake.save(plant().water().water())
        plantRepositoryFake.save(plantNeedsWaterNow())
        plantRepositoryFake.save(plantToUndoWatering)
        plantRepositoryFake.save(plantForgotten())

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            component.onFilterChange(PlantTabFilter.HISTORY)
            assertThat(awaitItem()).hasSize(3)

            component.onUndoWater(plantToUndoWatering)
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
        val plantsFlow = component.plants
        val plantToUndoWatering = plant().water().water()

        plantRepositoryFake.save(plantNeedsWaterNow())
        plantRepositoryFake.save(plant().water())
        plantRepositoryFake.save(plant().water().water())
        plantRepositoryFake.save(plantNeedsWaterNow())
        plantRepositoryFake.save(plantToUndoWatering)
        plantRepositoryFake.save(plantForgotten())

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            component.onFilterChange(PlantTabFilter.HISTORY)
            assertThat(awaitItem()).hasSize(3)

            component.onUndoWater(plantToUndoWatering)
            assertThat(awaitItem()).hasSize(3)
        }
    }

    // note test a delete on each filter    , i,e, 3 deletes and 3 assserts
    @Test
    fun `delete a plant removes it from the current list`() = runTest {
        // SETUP
        val plantsFlow = component.plants
        val upcomingPlant = plantNeedsWaterNow()
        val forgottenPlant = plantForgotten()
        val wateredPlant = plant().water().water()

        plantRepositoryFake.save(upcomingPlant)
        plantRepositoryFake.save(wateredPlant)
        plantRepositoryFake.save(forgottenPlant)
        plantRepositoryFake.save(plant().water().water())
        plantRepositoryFake.save(plantForgotten())

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            assertThat(awaitItem()).hasSize(1)
            component.onDeletePlant(upcomingPlant)
            assertThat(awaitItem()).hasSize(0)

            component.onFilterChange(PlantTabFilter.FORGOT_TO_WATER)
            assertThat(awaitItem()).hasSize(2)
            component.onDeletePlant(forgottenPlant)
            assertThat(awaitItem()).hasSize(1)

            component.onFilterChange(PlantTabFilter.HISTORY)
            assertThat(awaitItem()).hasSize(2)
            component.onDeletePlant(wateredPlant)
            assertThat(awaitItem()).hasSize(1)
        }
    }

    // note test a delete on each filter    , i,e, 3 deletes and 3 assserts
    @Test
    fun `undo deleting a plant returns it to the list it was deleted from`() = runTest {
        // SETUP
        val plantsFlow = component.plants
        val upcomingPlant = plantNeedsWaterNow()
        val forgottenPlant = plantForgotten()
        val wateredPlant = plant().water().water()

        plantRepositoryFake.save(upcomingPlant)
        plantRepositoryFake.save(wateredPlant)
        plantRepositoryFake.save(forgottenPlant)
        plantRepositoryFake.save(plant().water().water())
        plantRepositoryFake.save(plantForgotten())

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            assertThat(awaitItem()).hasSize(1)
            component.onDeletePlant(upcomingPlant)
            assertThat(awaitItem()).hasSize(0)
            component.onUndoDelete()
            assertThat(awaitItem()).hasSize(1)

            component.onFilterChange(PlantTabFilter.FORGOT_TO_WATER)
            assertThat(awaitItem()).hasSize(2)
            component.onDeletePlant(forgottenPlant)
            assertThat(awaitItem()).hasSize(1)
            component.onUndoDelete()
            assertThat(awaitItem()).hasSize(2)

            component.onFilterChange(PlantTabFilter.HISTORY)
            assertThat(awaitItem()).hasSize(2)
            component.onDeletePlant(wateredPlant)
            assertThat(awaitItem()).hasSize(1)
            component.onUndoDelete()
            assertThat(awaitItem()).hasSize(2)
        }
    }

    @Test
    fun `upcoming plants are sorted in ascending order by the time it needs water`() = runTest {
        // SETUP
        val plantsFlow = component.plants
        val today = today()
        val plantMorning = plantNeedsWaterNow(today.withHour(8))
        val plantAfternoon = plantNeedsWaterNow(today.withHour(12))
        val plantEvening = plantNeedsWaterNow(today.withHour(17))

        plantRepositoryFake.save(plantAfternoon)
        plantRepositoryFake.save(plantEvening)
        plantRepositoryFake.save(plantMorning)

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            assertThat(awaitItem()).containsExactly(plantMorning, plantAfternoon, plantEvening)
        }
    }

    @Test
    fun `forgot to water plants are sorted in ascending order by the date it needs water`() = runTest {
        // SETUP
        val plantsFlow = component.plants
        val today = today()
        val plantMorning = plantForgotten(today.withHour(8))
        val plantAfternoon = plantForgotten(today.withHour(12))
        val plantEvening = plantForgotten(today.withHour(17))

        plantRepositoryFake.save(plantAfternoon)
        plantRepositoryFake.save(plantMorning)
        plantRepositoryFake.save(plantEvening)

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            component.onFilterChange(PlantTabFilter.FORGOT_TO_WATER)
            assertThat(awaitItem()).containsExactly(plantMorning, plantAfternoon, plantEvening)
        }
    }

    @Test
    fun `history plants are sorted in descending order by the latest date it was watered`() = runTest {
        // SETUP
        val plantsFlow = component.plants
        val wateredThird = plant()
        val wateredSecond = plant()
        val wateredFirst = plant()

        mutableClock.reverseTimeBy(1.hours.toJavaDuration())
        plantRepositoryFake.save(wateredThird.water(mutableClock))

        mutableClock.reverseTimeBy(1.hours.toJavaDuration())
        plantRepositoryFake.save(wateredSecond.water(mutableClock))

        mutableClock.reverseTimeBy(1.hours.toJavaDuration())
        plantRepositoryFake.save(wateredFirst.water(mutableClock))

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            component.onFilterChange(PlantTabFilter.HISTORY)
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
        val plantsFlow = component.plants
        var plantToUndo = plant()

        // now is 4pm
        // water at 2pm
        mutableClock.reverseTimeBy(2.hours.toJavaDuration())
        plantRepositoryFake.save(plant().water(mutableClock))

        // water at 1pm
        mutableClock.reverseTimeBy(1.hours.toJavaDuration())
        plantToUndo = plantToUndo.water(mutableClock)
        plantRepositoryFake.save(plantToUndo)

        // water at 11am
        mutableClock.reverseTimeBy(2.hours.toJavaDuration())
        plantRepositoryFake.save(plant().water(mutableClock))

        // water at 3pm
        mutableClock.reset()
        mutableClock.reverseTimeBy(1.hours.toJavaDuration())
        plantToUndo = plantToUndo.water(mutableClock)
        plantRepositoryFake.save(plantToUndo)

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem()

            component.onFilterChange(PlantTabFilter.HISTORY)

            val emission = awaitItem()
            assertThat(emission).hasSize(3)
            assertThat(emission.first().dateLastWatered?.hour).isEqualTo(now().minusHours(1).hour) // 3pm
            assertThat(emission[1].dateLastWatered?.hour).isEqualTo(now().minusHours(2).hour) // 2pm
            assertThat(emission[2].dateLastWatered?.hour).isEqualTo(now().minusHours(5).hour) // 11am

            component.onUndoWater(plantToUndo)

            val emission2 = awaitItem()
            assertThat(emission2.first().dateLastWatered?.hour).isEqualTo(now().minusHours(2).hour) // 2pm
            assertThat(emission2[1].dateLastWatered?.hour).isEqualTo(now().minusHours(3).hour) // 1pm
            assertThat(emission2[2].dateLastWatered?.hour).isEqualTo(now().minusHours(5).hour) // 11am
        }
    }
}