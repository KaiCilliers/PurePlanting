package com.sunrisekcdeveloper.pureplanting.features.presentation.plants

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.sunrisekcdeveloper.pureplanting.features.component.plants.Plant
import com.sunrisekcdeveloper.shared_test.MutableClock
import com.sunrisekcdeveloper.shared_test.PlantCacheFake
import com.sunrisekcdeveloper.shared_test.advanceTimeBy
import com.sunrisekcdeveloper.shared_test.plant
import com.sunrisekcdeveloper.shared_test.today
import com.zhuinden.simplestack.Backstack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.days
import kotlin.time.toJavaDuration

@OptIn(ExperimentalCoroutinesApi::class)
class PlantsViewModelTest {

    private lateinit var plantCacheFake: PlantCacheFake
    private lateinit var viewModel: PlantsViewModel
    private lateinit var mutableClock: MutableClock

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        mutableClock = MutableClock(Clock.systemDefaultZone())
        plantCacheFake = PlantCacheFake()
        viewModel = PlantsViewModel(plantCacheFake, mutableClock, Backstack())
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        plantCacheFake.resetData()
    }

    @Test
    fun `fresh launch, screen empty plants list state emitted`() = runTest {
        // SETUP
        val plantsFlow: StateFlow<List<Plant>> = viewModel.plants

        // ACTION & ASSERTIONS
        plantsFlow.test {
            val emission1 = awaitItem()
            assertThat(emission1.isEmpty()).isTrue()
        }

    }

    @Test
    fun `add new plant, new plants list state emitted`() = runTest {
        // SETUP
        val plantsFlow = viewModel.plants

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem() // initial emission

            val plantToAdd = plant()
            plantCacheFake.save(plantToAdd)

            val emission2 = awaitItem()
            assertThat(emission2.size).isEqualTo(1)
            assertThat(emission2.first().id).isEqualTo(plantToAdd.id)
        }
    }

    @Test
    fun `by default, expect only plants that need to get watered soon`() = runTest {
        // SETUP
        val today = today(DayOfWeek.THURSDAY)
        var actualToday = LocalDateTime.now(mutableClock)

        while(actualToday.dayOfWeek != today.dayOfWeek) {
            mutableClock.advanceTimeBy(1.days.toJavaDuration())
            actualToday = LocalDateTime.now(mutableClock)
        }

        plantCacheFake.resetData(
            listOf(
                plant(waterDays = listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY)),
                plant(waterDays = listOf(DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY)),
                plant(waterDays = listOf(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)),
                plant(waterDays = listOf(DayOfWeek.FRIDAY, DayOfWeek.SUNDAY))
            ).map { it.nextWateringDate(today) }
        )
        val plantsFlow = viewModel.plants

        // ACTION & ASSERTIONS
        plantsFlow.test {
            awaitItem() // initial emission

            val emission2 = awaitItem()
            assertThat(emission2.size).isEqualTo(2)
        }
    }

    @Test
    fun `forgot to water filter selected, expect only plants that was forgotten`() = runTest {
        // SETUP
        val today = today()
        plantCacheFake.resetData(
            listOf(
                plant(nextWateringDate = today.plusDays(1)),
                plant(nextWateringDate = today.plusDays(2)),
                plant(nextWateringDate = today.plusDays(3)),
                plant(nextWateringDate = today.plusDays(4)),
                plant(nextWateringDate = today.plusDays(5)),
                plant(nextWateringDate = today.plusDays(6)),
            )
        )

        // ACTION & ASSERTIONS
        viewModel.plants.test {
            awaitItem()
            awaitItem()

            advanceTimeBy(2.days, mutableClock) // clock used by ViewModel
            viewModel.setFilter(PlantListFilter.FORGOT_TO_WATER)
            assertThat(awaitItem().size).isEqualTo(2)
        }

    }

    @Test
    fun `history filter selected, expect only plants that have a record of being watered`() = runTest {
        // SETUP
        plantCacheFake.resetData(
            listOf(
                plant(waterDays = listOf(DayOfWeek.FRIDAY, DayOfWeek.SUNDAY)).water(),
                plant(waterDays = listOf(DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY)).water(),
                plant(waterDays = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)),
                plant(waterDays = listOf(DayOfWeek.THURSDAY, DayOfWeek.TUESDAY)).water(),
                plant(waterDays = listOf(DayOfWeek.FRIDAY, DayOfWeek.MONDAY)),
            )
        )

        // ACTION & ASSERTIONS
        viewModel.plants.test {
            awaitItem()

            viewModel.setFilter(PlantListFilter.HISTORY)
            assertThat(awaitItem().size).isEqualTo(3)
        }
    }

    @Test
    fun `remove plant, current list of plants is refreshed`() = runTest {
        // SETUP
        val today = today()
        val allPlants = listOf(
            plant(nextWateringDate = today),
            plant(nextWateringDate = today),
            plant(nextWateringDate = today.plusDays(1)),
            plant(nextWateringDate = today),
            plant(nextWateringDate = today.plusDays(1)),
        )
        plantCacheFake.resetData(allPlants)

        // ACTION & ASSERTIONS
        viewModel.plants.test {
            awaitItem()

            viewModel.removePlant(allPlants.first().id)

            val emission = awaitItem()
            assertThat(emission.size).isEqualTo(allPlants.size - 1)
        }
    }

    @Test
    fun `undo previously removed plant, removed plant is returned and can be found in current plant list`() = runTest {
        // SETUP
        val today = today()
        val allPlants = listOf(
            plant(nextWateringDate = today),
            plant(nextWateringDate = today.plusDays(1)),
            plant(nextWateringDate = today),
        )
        val plantToRemove = allPlants.first()
        plantCacheFake.resetData(allPlants)

        // ACTION & ASSERTIONS
        viewModel.plants.test {
            awaitItem()

            viewModel.removePlant(plantToRemove.id)

            val emission = awaitItem()
            assertThat(emission.size).isEqualTo(allPlants.size - 1)

            viewModel.undoRemove(plantToRemove.id)
            val emission2 = awaitItem()
            assertThat(emission2.size).isEqualTo(allPlants.size)
            assertThat(emission2).contains(plantToRemove)
        }
    }

}