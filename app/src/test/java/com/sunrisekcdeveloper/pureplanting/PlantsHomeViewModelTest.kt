package com.sunrisekcdeveloper.pureplanting

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
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

@OptIn(ExperimentalCoroutinesApi::class)
class PlantsHomeViewModelTest {

    private lateinit var plantCacheFake: PlantCacheFake
    private lateinit var viewModel: PlantsHomeViewModel
    private lateinit var mutableClock: MutableClock

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        mutableClock = MutableClock(Clock.systemDefaultZone())
        plantCacheFake = PlantCacheFake()
        viewModel = PlantsHomeViewModel(plantCacheFake, mutableClock)
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
            viewModel.addPlant(plantToAdd)

            val emission2 = awaitItem()
            assertThat(emission2.size).isEqualTo(1)
            assertThat(emission2.first().id).isEqualTo(plantToAdd.id)
        }
    }

    @Test
    fun `by default, expect only plants that need to get watered soon`() = runTest {
        // SETUP
        plantCacheFake.resetData(
            listOf(
                plant(waterDays = listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY)),
                plant(waterDays = listOf(DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY)),
                plant(waterDays = listOf(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)),
                plant(waterDays = listOf(DayOfWeek.FRIDAY, DayOfWeek.SUNDAY))
            ).map { it.nextWateringDate(LocalDateTime.now()) }
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
        val today = today(DayOfWeek.THURSDAY, clock = mutableClock)
        plantCacheFake.resetData(
            listOf(
                plant(waterDays = listOf(DayOfWeek.FRIDAY, DayOfWeek.SUNDAY)),
                plant(waterDays = listOf(DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY)),
                plant(waterDays = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)),
                plant(waterDays = listOf(DayOfWeek.THURSDAY, DayOfWeek.TUESDAY)),
                plant(waterDays = listOf(DayOfWeek.FRIDAY, DayOfWeek.MONDAY)),
            ).map { it.nextWateringDate(today) }
        )

        // ACTION & ASSERTIONS
        viewModel.plants.test {
            awaitItem()
            awaitItem()

            advanceTimeBy(2.days, mutableClock) // move to Saturday
            viewModel.setFilter(PlantFilter.FORGOT)
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

            viewModel.setFilter(PlantFilter.HISTORY)
            assertThat(awaitItem().size).isEqualTo(3)
        }
    }

}