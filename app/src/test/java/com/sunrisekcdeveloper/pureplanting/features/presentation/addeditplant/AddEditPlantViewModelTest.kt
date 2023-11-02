package com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isZero
import com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant.components.PlantSize
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
import java.time.LocalTime

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
        assertThat(viewModel.image.value).isEqualTo("")
        assertThat(viewModel.name.value).isEqualTo("")
        assertThat(viewModel.description.value).isEqualTo("")
        assertThat(viewModel.size.value).isEqualTo(AddEditPlantViewModel.DEFAULT_PLANT_SIZE)
        assertThat(viewModel.wateringDays.value).isEqualTo(listOf(AddEditPlantViewModel.DEFAULT_WATERING_DAY))
        assertThat(viewModel.wateringTime.value.hour).isEqualTo(AddEditPlantViewModel.DEFAULT_WATERING_HOUR)
        assertThat(viewModel.wateringTime.value.minute).isEqualTo(AddEditPlantViewModel.DEFAULT_WATERING_MIN)
        assertThat(viewModel.wateringAmount.value).isEqualTo(AddEditPlantViewModel.DEFAULT_WATERING_AMOUNT)
    }

    @Test
    fun `with initial plant, all plant values are set to that plant's details`() = runTest {
        // SETUP
        val initialPlant = plant()
        val viewModel = AddEditPlantViewModel(plantCacheFake, initialPlant)

        // ASSERTIONS
        assertThat(viewModel.image.value).isEqualTo(initialPlant.details.imageSrcUri)
        assertThat(viewModel.name.value).isEqualTo(initialPlant.details.name)
        assertThat(viewModel.description.value).isEqualTo(initialPlant.details.description)
        assertThat(viewModel.size.value.name).isEqualTo(initialPlant.details.size)
        assertThat(viewModel.wateringDays.value).isEqualTo(initialPlant.wateringInfo.days)
        assertThat(viewModel.wateringTime.value.hour).isEqualTo(initialPlant.wateringInfo.atHour)
        assertThat(viewModel.wateringTime.value.minute).isEqualTo(initialPlant.wateringInfo.atMin)
        assertThat(viewModel.wateringAmount.value).isEqualTo(initialPlant.wateringInfo.amount)
    }

    @Test
    fun `saving a plant without an initial plant passed, creates a new plant`() = runTest {
        // SETUP
        val viewModel = AddEditPlantViewModel(plantCacheFake, null)

        // ACTION
        viewModel.image.value = "img"
        viewModel.name.value = "test 1"
        viewModel.description.value = "test 1 desc"
        viewModel.size.value = PlantSize.Medium
        viewModel.wateringDays.value =  listOf(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        viewModel.wateringTime.value = LocalTime.of(14, 0)
        viewModel.wateringAmount.value = "400ml"

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
        viewModel.image.value = "img"
        viewModel.name.value = "test 1"
        viewModel.description.value = "test 1 desc"
        viewModel.size.value = PlantSize.Medium
        viewModel.wateringDays.value =  listOf(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        viewModel.wateringTime.value = LocalTime.of(14, 0)
        viewModel.wateringAmount.value = "400ml"

        // ASSERTIONS
        assertThat(plantCacheFake.all().size).isEqualTo(1)

        viewModel.savePlant()
        advanceTimeBy(10)

        val updatedPlant = plantCacheFake.find(initialPlant.id)
        assertThat(plantCacheFake.all().size).isEqualTo(1)
        assertThat(updatedPlant).isNotNull()
        assertThat(updatedPlant!!.details.imageSrcUri).isEqualTo(viewModel.image.value)
        assertThat(updatedPlant.details.name).isEqualTo(viewModel.name.value)
        assertThat(updatedPlant.details.description).isEqualTo(viewModel.description.value)
        assertThat(updatedPlant.details.size).isEqualTo(viewModel.size.value.name)
        assertThat(updatedPlant.wateringInfo.days).isEqualTo(viewModel.wateringDays.value)
        assertThat(updatedPlant.wateringInfo.atHour).isEqualTo(viewModel.wateringTime.value.hour)
        assertThat(updatedPlant.wateringInfo.atMin).isEqualTo(viewModel.wateringTime.value.minute)
        assertThat(updatedPlant.wateringInfo.amount).isEqualTo(viewModel.wateringAmount.value)
    }

}