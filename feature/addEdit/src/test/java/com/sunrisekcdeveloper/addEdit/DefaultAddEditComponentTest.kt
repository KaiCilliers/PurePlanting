package com.sunrisekcdeveloper.addEdit

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotNull
import assertk.assertions.isZero
import com.sunrisekcdeveloper.plant.domain.PlantRepository
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
class DefaultAddEditComponentTest {

    private lateinit var plantRepositoryFake: PlantRepository.Fake
    private lateinit var router: Router

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        plantRepositoryFake = PlantRepository.Fake()
        router = object : Router {
            override fun jumpToRoot() { }
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
        val component = DefaultAddEditComponent(
            plantRepository = plantRepositoryFake,
            plant = null,
            router = router
        )

        // ASSERTIONS
        assertThat(component.image.value).isEqualTo("")
        assertThat(component.name.value).isEqualTo("")
        assertThat(component.description.value).isEqualTo("")
        assertThat(component.size.value).isEqualTo(DefaultAddEditComponent.DEFAULT_PLANT_SIZE)
        assertThat(component.wateringDays.value).isEqualTo(listOf(DefaultAddEditComponent.DEFAULT_WATERING_DAY))
        assertThat(component.wateringTime.value.hour).isEqualTo(DefaultAddEditComponent.DEFAULT_WATERING_TIME.hour)
        assertThat(component.wateringTime.value.minute).isEqualTo(DefaultAddEditComponent.DEFAULT_WATERING_TIME.minute)
        assertThat(component.wateringAmount.value).isEqualTo(DefaultAddEditComponent.DEFAULT_WATERING_AMOUNT)
    }

    @Test
    fun `editing and existing plant sets input fields to plant's values`() = runTest {
        // SETUP
        val initialPlant = plant()
        val component = DefaultAddEditComponent(plantRepositoryFake, router, initialPlant)

        // ASSERTIONS
        assertThat(component.image.value).isEqualTo(initialPlant.details.imageSrcUri)
        assertThat(component.name.value).isEqualTo(initialPlant.details.name)
        assertThat(component.description.value).isEqualTo(initialPlant.details.description)
        assertThat(component.size.value.name).isEqualTo(initialPlant.details.size)
        assertThat(component.wateringDays.value).isEqualTo(initialPlant.wateringInfo.days)
        assertThat(component.wateringTime.value.hour).isEqualTo(initialPlant.wateringInfo.time.hour)
        assertThat(component.wateringTime.value.minute).isEqualTo(initialPlant.wateringInfo.time.minute)
        assertThat(component.wateringAmount.value).isEqualTo(initialPlant.wateringInfo.amount)
    }

    @Test
    fun `save changes without an initial plant creates a new plant`() = runTest {
        // SETUP
        val component = DefaultAddEditComponent(plantRepositoryFake, router, null)

        // ACTION
        component.image.value = "img"
        component.name.value = "test 1"
        component.description.value = "test 1 desc"
        component.size.value = PlantSize.Medium
        component.wateringDays.value = listOf(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        component.wateringTime.value = LocalTime.of(14, 0)
        component.wateringAmount.value = "400ml"

        // ASSERTIONS
        assertThat(plantRepositoryFake.all().size).isZero()

        component.onSavePlant()
        advanceTimeBy(10) // small delay to complete saving of plant
        assertThat(plantRepositoryFake.all().size).isEqualTo(1)
    }

    @Test
    fun `save changes with an initial plant updates existing plants with input field values`() = runTest {
        // SETUP
        val initialPlant = plant()
        plantRepositoryFake.save(initialPlant)
        val component = DefaultAddEditComponent(plantRepositoryFake, router, initialPlant)

        // ACTION
        component.image.value = "img"
        component.name.value = "test 1"
        component.description.value = "test 1 desc"
        component.size.value = PlantSize.Medium
        component.wateringDays.value = listOf(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        component.wateringTime.value = LocalTime.of(14, 0)
        component.wateringAmount.value = "400ml"

        // ASSERTIONS
        assertThat(plantRepositoryFake.all().size).isEqualTo(1)

        component.onSavePlant()
        advanceTimeBy(10)

        val updatedPlant = plantRepositoryFake.find(initialPlant.id)
        assertThat(plantRepositoryFake.all().size).isEqualTo(1)
        assertThat(updatedPlant).isNotNull()
        assertThat(updatedPlant!!.details.imageSrcUri).isEqualTo(component.image.value)
        assertThat(updatedPlant.details.name).isEqualTo(component.name.value)
        assertThat(updatedPlant.details.description).isEqualTo(component.description.value)
        assertThat(updatedPlant.details.size).isEqualTo(component.size.value.name)
        assertThat(updatedPlant.wateringInfo.days).isEqualTo(component.wateringDays.value)
        assertThat(updatedPlant.wateringInfo.time.hour).isEqualTo(component.wateringTime.value.hour)
        assertThat(updatedPlant.wateringInfo.time.minute).isEqualTo(component.wateringTime.value.minute)
        assertThat(updatedPlant.wateringInfo.amount).isEqualTo(component.wateringAmount.value)
    }

    @Test
    fun `save changes with an initial plant updates existing plants last modified date when water days have changed`() = runTest {
        // SETUP
        val initialPlant = plant(
            waterDays = listOf(DayOfWeek.MONDAY)
        )
        plantRepositoryFake.save(initialPlant)
        val component = DefaultAddEditComponent(plantRepositoryFake, router, initialPlant)

        // ACTION
        component.wateringDays.value = listOf(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)

        // ASSERTIONS
        assertThat(plantRepositoryFake.all().size).isEqualTo(1)

        component.onSavePlant()
        advanceTimeBy(10)

        val updatedPlant = plantRepositoryFake.find(initialPlant.id)
        assertThat(plantRepositoryFake.all().size).isEqualTo(1)
        assertThat(updatedPlant).isNotNull()
        assertThat(updatedPlant!!.wateringInfo.daysLastModified).isGreaterThan(initialPlant.wateringInfo.daysLastModified)
    }

    @Test
    fun `save changes with an initial plant does not updates existing plants last modified date when water days have not changed`() = runTest {
        // SETUP
        val initialPlant = plant(
            waterDays = listOf(DayOfWeek.MONDAY)
        )
        plantRepositoryFake.save(initialPlant)
        val component = DefaultAddEditComponent(plantRepositoryFake, router, initialPlant)

        // ACTION
        component.image.value = "img"
        component.name.value = "test 1"
        component.description.value = "test 1 desc"
        component.size.value = PlantSize.Medium
        component.wateringTime.value = LocalTime.of(14, 0)
        component.wateringAmount.value = "400ml"

        // ASSERTIONS
        assertThat(plantRepositoryFake.all().size).isEqualTo(1)

        component.onSavePlant()
        advanceTimeBy(10)

        val updatedPlant = plantRepositoryFake.find(initialPlant.id)
        assertThat(plantRepositoryFake.all().size).isEqualTo(1)
        assertThat(updatedPlant).isNotNull()
        assertThat(updatedPlant!!.wateringInfo.daysLastModified).isEqualTo(initialPlant.wateringInfo.daysLastModified)
    }

}