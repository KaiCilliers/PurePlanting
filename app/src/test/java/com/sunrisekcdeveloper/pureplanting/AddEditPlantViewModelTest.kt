package com.sunrisekcdeveloper.pureplanting

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isZero
import com.sunrisekcdeveloper.shared_test.PlantCacheFake
import com.sunrisekcdeveloper.shared_test.plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.DayOfWeek

@OptIn(ExperimentalCoroutinesApi::class)
class AddEditPlantViewModelTest {

    private lateinit var plantCacheFake: PlantCacheFake

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        plantCacheFake = PlantCacheFake()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        plantCacheFake.resetData()
    }

    @Test
    fun `without initial plant, all plant values are set to default values`() = runTest {
        // SETUP
        val viewModel = AddEditPlantViewModel(plantCacheFake, null)

        // ASSERTIONS
        assertThat(viewModel.image).isEqualTo("")
        assertThat(viewModel.name).isEqualTo("")
        assertThat(viewModel.description).isEqualTo("")
        assertThat(viewModel.size).isEqualTo("")
        assertThat(viewModel.wateringDays).isEmpty()
        assertThat(viewModel.wateringHour).isEqualTo(AddEditPlantViewModel.DEFAULT_WATERING_HOUR)
        assertThat(viewModel.wateringAmount).isEqualTo(AddEditPlantViewModel.DEFAULT_WATERING_AMOUNT)
    }

    @Test
    fun `with initial plant, all plant values are set to that plant's details`() = runTest {
        // SETUP
        val initialPlant = plant()
        val viewModel = AddEditPlantViewModel(plantCacheFake, initialPlant)

        // ASSERTIONS
        assertThat(viewModel.image).isEqualTo(initialPlant.details.imageSrcUri)
        assertThat(viewModel.name).isEqualTo(initialPlant.details.name)
        assertThat(viewModel.description).isEqualTo(initialPlant.details.description)
        assertThat(viewModel.size).isEqualTo(initialPlant.details.size)
        assertThat(viewModel.wateringDays).isEqualTo(initialPlant.wateringInfo.days)
        assertThat(viewModel.wateringHour).isEqualTo(initialPlant.wateringInfo.atHour)
        assertThat(viewModel.wateringAmount).isEqualTo(initialPlant.wateringInfo.amount)
    }

    @Test
    fun `saving a plant without an initial plant passed, creates a new plant`() = runTest {
        // SETUP
        val viewModel = AddEditPlantViewModel(plantCacheFake, null)

        // ACTION
        viewModel.image = "img"
        viewModel.name = "test 1"
        viewModel.description = "test 1 desc"
        viewModel.size = "medium"
        viewModel.wateringDays =  listOf(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        viewModel.wateringHour = 14
        viewModel.wateringAmount = "400ml"

        // ASSERTIONS
        assertThat(plantCacheFake.all().size).isZero()

        viewModel.savePlant()
        advanceTimeBy(10) // small delay to complete saving of plant
        assertThat(plantCacheFake.all().size).isEqualTo(1)
    }

    @Test
    fun `saving a plant with an initial plant passed, updates existing plant`() = runTest {
        val initialPlant = plant()
        plantCacheFake.save(initialPlant)
        val viewModel = AddEditPlantViewModel(plantCacheFake, initialPlant)

        // ACTION
        viewModel.image = "img"
        viewModel.name = "test 1"
        viewModel.description = "test 1 desc"
        viewModel.size = "medium"
        viewModel.wateringDays =  listOf(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        viewModel.wateringHour = 14
        viewModel.wateringAmount = "400ml"

        // ASSERTIONS
        assertThat(plantCacheFake.all().size).isEqualTo(1)

        viewModel.savePlant()
        advanceTimeBy(10)

        val updatedPlant = plantCacheFake.find(initialPlant.id)
        assertThat(plantCacheFake.all().size).isEqualTo(1)
        assertThat(updatedPlant).isNotNull()
        assertThat(updatedPlant!!.details.imageSrcUri).isEqualTo(viewModel.image)
        assertThat(updatedPlant.details.name).isEqualTo(viewModel.name)
        assertThat(updatedPlant.details.description).isEqualTo(viewModel.description)
        assertThat(updatedPlant.details.size).isEqualTo(viewModel.size)
        assertThat(updatedPlant.wateringInfo.days).isEqualTo(viewModel.wateringDays)
        assertThat(updatedPlant.wateringInfo.atHour).isEqualTo(viewModel.wateringHour)
        assertThat(updatedPlant.wateringInfo.amount).isEqualTo(viewModel.wateringAmount)
    }

}