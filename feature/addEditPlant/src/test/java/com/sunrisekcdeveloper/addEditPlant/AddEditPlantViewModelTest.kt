package com.sunrisekcdeveloper.addEditPlant

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotNull
import assertk.assertions.isZero
import com.sunrisekcdeveloper.android.toParcelable
import com.sunrisekcdeveloper.plant.PlantCacheFake
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
    private lateinit var router: Router

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        plantCacheFake = PlantCacheFake()
        router = object : Router {
            override fun jumpToRoot() { }
        }
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        plantCacheFake.resetData()
    }

    @Test
    fun `adding a new plant sets all input fields to their default values`() = runTest {
        // SETUP
        val viewModel = AddEditPlantViewModel(
            plantCache = plantCacheFake,
            parcelablePlant = null,
            router = router
        )

        // ASSERTIONS
        assertThat(viewModel.image.value).isEqualTo("")
        assertThat(viewModel.name.value).isEqualTo("")
        assertThat(viewModel.description.value).isEqualTo("")
        assertThat(viewModel.size.value).isEqualTo(AddEditPlantViewModel.DEFAULT_PLANT_SIZE)
        assertThat(viewModel.wateringDays.value).isEqualTo(listOf(AddEditPlantViewModel.DEFAULT_WATERING_DAY))
        assertThat(viewModel.wateringTime.value.hour).isEqualTo(AddEditPlantViewModel.DEFAULT_WATERING_TIME.hour)
        assertThat(viewModel.wateringTime.value.minute).isEqualTo(AddEditPlantViewModel.DEFAULT_WATERING_TIME.minute)
        assertThat(viewModel.wateringAmount.value).isEqualTo(AddEditPlantViewModel.DEFAULT_WATERING_AMOUNT)
    }

    @Test
    fun `editing and existing plant sets input fields to plant's values`() = runTest {
        // SETUP
        val initialPlant = plant()
        val viewModel = AddEditPlantViewModel(plantCacheFake, initialPlant.toParcelable(), router)

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
        val viewModel = AddEditPlantViewModel(plantCacheFake, null, router)

        // ACTION
        viewModel.image.value = "img"
        viewModel.name.value = "test 1"
        viewModel.description.value = "test 1 desc"
        viewModel.size.value = PlantSize.Medium
        viewModel.wateringDays.value = listOf(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        viewModel.wateringTime.value = LocalTime.of(14, 0)
        viewModel.wateringAmount.value = "400ml"

        // ASSERTIONS
        assertThat(plantCacheFake.all().size).isZero()

        viewModel.savePlant()
        advanceTimeBy(10) // small delay to complete saving of plant
        assertThat(plantCacheFake.all().size).isEqualTo(1)
    }

    @Test
    fun `save changes with an initial plant updates existing plants with input field values`() = runTest {
        // SETUP
        val initialPlant = plant()
        plantCacheFake.save(initialPlant)
        val viewModel = AddEditPlantViewModel(plantCacheFake, initialPlant.toParcelable(), router)

        // ACTION
        viewModel.image.value = "img"
        viewModel.name.value = "test 1"
        viewModel.description.value = "test 1 desc"
        viewModel.size.value = PlantSize.Medium
        viewModel.wateringDays.value = listOf(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
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
        assertThat(updatedPlant.wateringInfo.time.hour).isEqualTo(viewModel.wateringTime.value.hour)
        assertThat(updatedPlant.wateringInfo.time.minute).isEqualTo(viewModel.wateringTime.value.minute)
        assertThat(updatedPlant.wateringInfo.amount).isEqualTo(viewModel.wateringAmount.value)
    }

    @Test
    fun `save changes with an initial plant updates existing plants last modified date value`() = runTest {
        // SETUP
        val initialPlant = plant()
        plantCacheFake.save(initialPlant)
        val viewModel = AddEditPlantViewModel(plantCacheFake, initialPlant.toParcelable(), router)

        // ACTION
        viewModel.image.value = "img"
        viewModel.name.value = "test 1"
        viewModel.description.value = "test 1 desc"
        viewModel.size.value = PlantSize.Medium
        viewModel.wateringDays.value = listOf(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        viewModel.wateringTime.value = LocalTime.of(14, 0)
        viewModel.wateringAmount.value = "400ml"

        // ASSERTIONS
        assertThat(plantCacheFake.all().size).isEqualTo(1)

        viewModel.savePlant()
        advanceTimeBy(10)

        val updatedPlant = plantCacheFake.find(initialPlant.id)
        assertThat(plantCacheFake.all().size).isEqualTo(1)
        assertThat(updatedPlant).isNotNull()
        assertThat(updatedPlant!!.userLastModifiedDate).isGreaterThan(initialPlant.userLastModifiedDate)
    }

}