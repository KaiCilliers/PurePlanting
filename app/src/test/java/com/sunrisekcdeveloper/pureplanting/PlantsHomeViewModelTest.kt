package com.sunrisekcdeveloper.pureplanting

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

class PlantsHomeViewModelTest {

    private lateinit var viewModel: PlantsHomeViewModel

    @BeforeEach
    fun setup() {
        viewModel = PlantsHomeViewModel()
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
    fun `by default, show only plants that need to get watered in the future`() = runTest {
        // SETUP

        // ACTION
        
        // ASSERTIONS
    
    }

}