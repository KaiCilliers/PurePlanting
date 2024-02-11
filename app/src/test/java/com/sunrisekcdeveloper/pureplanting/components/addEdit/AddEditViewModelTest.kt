package com.sunrisekcdeveloper.home.com.sunrisekcdeveloper.pureplanting.components.addEdit

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotNull
import assertk.assertions.isZero
import com.sunrisekcdeveloper.pureplanting.core.design.ui.SnackbarEmitter
import com.sunrisekcdeveloper.pureplanting.business.plant.PlantRepository
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
class AddEditViewModelTest {

    private lateinit var plantRepositoryFake: PlantRepository.Fake
    private lateinit var router: com.sunrisekcdeveloper.pureplanting.components.addEdit.AddEditViewModel.Router
    private lateinit var eventEmitter: SnackbarEmitter

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        eventEmitter = SnackbarEmitter()
        plantRepositoryFake = PlantRepository.Fake()
        router = object : com.sunrisekcdeveloper.pureplanting.components.addEdit.AddEditViewModel.Router {
            override fun jumpToRoot() { }
            override fun goBack() { }
        }
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        plantRepositoryFake.resetData()
    }

    @Test
    fun `adding a new plant sets all input fields to their default values`() = runTest {
        // SETUP
        val viewModel = com.sunrisekcdeveloper.pureplanting.components.addEdit.AddEditViewModel.Default(
            plantRepository = plantRepositoryFake,
            plant = null,
            router = router,
            eventEmitter = eventEmitter
        )

        // ASSERTIONS
        assertThat(viewModel.image.value).isEqualTo("")
        assertThat(viewModel.name.value).isEqualTo("")
        assertThat(viewModel.description.value).isEqualTo("")
        assertThat(viewModel.size.value).isEqualTo(com.sunrisekcdeveloper.pureplanting.components.addEdit.AddEditViewModel.Default.DEFAULT_PLANT_SIZE)
        assertThat(viewModel.wateringDays.value).isEqualTo(listOf(com.sunrisekcdeveloper.pureplanting.components.addEdit.AddEditViewModel.Default.DEFAULT_WATERING_DAY))
        assertThat(viewModel.wateringTime.value.hour).isEqualTo(com.sunrisekcdeveloper.pureplanting.components.addEdit.AddEditViewModel.Default.DEFAULT_WATERING_TIME.hour)
        assertThat(viewModel.wateringTime.value.minute).isEqualTo(com.sunrisekcdeveloper.pureplanting.components.addEdit.AddEditViewModel.Default.DEFAULT_WATERING_TIME.minute)
        assertThat(viewModel.wateringAmount.value).isEqualTo(com.sunrisekcdeveloper.pureplanting.components.addEdit.AddEditViewModel.Default.DEFAULT_WATERING_AMOUNT)
    }

    @Test
    fun `editing and existing plant sets input fields to plant's values`() = runTest {
        // SETUP
        val initialPlant = plant()
        val viewModel = com.sunrisekcdeveloper.pureplanting.components.addEdit.AddEditViewModel.Default(plantRepositoryFake, router, eventEmitter, initialPlant)

        // ASSERTIONS
        assertThat(viewModel.image.value).isEqualTo(initialPlant.details.imageSrcUri)
        assertThat(viewModel.name.value).isEqualTo(initialPlant.details.name)
        assertThat(viewModel.description.value).isEqualTo(initialPlant.details.description)
        assertThat(viewModel.size.value.name).isEqualTo(initialPlant.details.size)
        assertThat(viewModel.wateringDays.value).isEqualTo(initialPlant.wateringInfo.days)
        assertThat(viewModel.wateringTime.value.hour).isEqualTo(initialPlant.wateringInfo.time.hour)
        assertThat(viewModel.wateringTime.value.minute).isEqualTo(initialPlant.wateringInfo.time.minute)
        assertThat(viewModel.wateringAmount.value).isEqualTo(initialPlant.wateringInfo.amount)
    }

    @Test
    fun `save changes without an initial plant creates a new plant`() = runTest {
        // SETUP
        val viewModel = com.sunrisekcdeveloper.pureplanting.components.addEdit.AddEditViewModel.Default(plantRepositoryFake, router, eventEmitter, null)

        // ACTION
        viewModel.image.value = "img"
        viewModel.name.value = "test 1"
        viewModel.description.value = "test 1 desc"
        viewModel.size.value = com.sunrisekcdeveloper.pureplanting.components.addEdit.models.PlantSize.Medium
        viewModel.wateringDays.value = listOf(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        viewModel.wateringTime.value = LocalTime.of(14, 0)
        viewModel.wateringAmount.value = "400ml"

        // ASSERTIONS
        assertThat(plantRepositoryFake.all().size).isZero()

        viewModel.onSavePlant()
        advanceTimeBy(10) // small delay to complete saving of plant
        assertThat(plantRepositoryFake.all().size).isEqualTo(1)
    }

    @Test
    fun `save changes with an initial plant updates existing plants with input field values`() = runTest {
        // SETUP
        val initialPlant = plant()
        plantRepositoryFake.save(initialPlant)
        val viewModel = com.sunrisekcdeveloper.pureplanting.components.addEdit.AddEditViewModel.Default(plantRepositoryFake, router, eventEmitter, initialPlant)

        // ACTION
        viewModel.image.value = "img"
        viewModel.name.value = "test 1"
        viewModel.description.value = "test 1 desc"
        viewModel.size.value = com.sunrisekcdeveloper.pureplanting.components.addEdit.models.PlantSize.Medium
        viewModel.wateringDays.value = listOf(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        viewModel.wateringTime.value = LocalTime.of(14, 0)
        viewModel.wateringAmount.value = "400ml"

        // ASSERTIONS
        assertThat(plantRepositoryFake.all().size).isEqualTo(1)

        viewModel.onSavePlant()
        advanceTimeBy(10)

        val updatedPlant = plantRepositoryFake.find(initialPlant.id)
        assertThat(plantRepositoryFake.all().size).isEqualTo(1)
        assertThat(updatedPlant).isNotNull()
        assertThat(updatedPlant!!.details.imageSrcUri).isEqualTo(viewModel.image.value)
        assertThat(updatedPlant.details.name).isEqualTo(viewModel.name.value)
        assertThat(updatedPlant.details.description).isEqualTo(viewModel.description.value)
        assertThat(updatedPlant.details.size).isEqualTo(viewModel.size.value.name)
        assertThat(updatedPlant.wateringInfo.days).isEqualTo(viewModel.wateringDays.value)
        assertThat(updatedPlant.wateringInfo.time.hour).isEqualTo(viewModel.wateringTime.value.hour)
        assertThat(updatedPlant.wateringInfo.time.minute).isEqualTo(viewModel.wateringTime.value.minute)
        assertThat(updatedPlant.wateringInfo.amount).isEqualTo(viewModel.wateringAmount.value)
    }

    @Test
    fun `save changes with an initial plant updates existing plants last modified date when water days have changed`() = runTest {
        // SETUP
        val initialPlant = plant(
            waterDays = listOf(DayOfWeek.MONDAY)
        )
        plantRepositoryFake.save(initialPlant)
        val viewModel = com.sunrisekcdeveloper.pureplanting.components.addEdit.AddEditViewModel.Default(plantRepositoryFake, router, eventEmitter, initialPlant)

        // ACTION
        viewModel.wateringDays.value = listOf(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)

        // ASSERTIONS
        assertThat(plantRepositoryFake.all().size).isEqualTo(1)

        viewModel.onSavePlant()
        advanceTimeBy(10)

        val updatedPlant = plantRepositoryFake.find(initialPlant.id)
        assertThat(plantRepositoryFake.all().size).isEqualTo(1)
        assertThat(updatedPlant).isNotNull()
        assertThat(updatedPlant!!.wateringInfo.lastModifiedWateringDays).isGreaterThan(initialPlant.wateringInfo.lastModifiedWateringDays)
    }

    @Test
    fun `save changes with an initial plant does not updates existing plants last modified date when water days have not changed`() = runTest {
        // SETUP
        val initialPlant = plant(
            waterDays = listOf(DayOfWeek.MONDAY)
        )
        plantRepositoryFake.save(initialPlant)
        val viewModel = com.sunrisekcdeveloper.pureplanting.components.addEdit.AddEditViewModel.Default(plantRepositoryFake, router, eventEmitter, initialPlant)

        // ACTION
        viewModel.image.value = "img"
        viewModel.name.value = "test 1"
        viewModel.description.value = "test 1 desc"
        viewModel.size.value = com.sunrisekcdeveloper.pureplanting.components.addEdit.models.PlantSize.Medium
        viewModel.wateringTime.value = LocalTime.of(14, 0)
        viewModel.wateringAmount.value = "400ml"

        // ASSERTIONS
        assertThat(plantRepositoryFake.all().size).isEqualTo(1)

        viewModel.onSavePlant()
        advanceTimeBy(10)

        val updatedPlant = plantRepositoryFake.find(initialPlant.id)
        assertThat(plantRepositoryFake.all().size).isEqualTo(1)
        assertThat(updatedPlant).isNotNull()
        assertThat(updatedPlant!!.wateringInfo.lastModifiedWateringDays).isEqualTo(initialPlant.wateringInfo.lastModifiedWateringDays)
    }

}