package com.sunrisekcdeveloper.pureplanting.features.presentation.plants

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.sunrisekcdeveloper.pureplanting.features.component.plants.Plant
import com.sunrisekcdeveloper.shared_test.MutableClock
import com.sunrisekcdeveloper.shared_test.plant
import com.sunrisekcdeveloper.shared_test.today
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.DayOfWeek
import kotlin.time.Duration.Companion.hours
import kotlin.time.toJavaDuration


class PlantTest {

    private lateinit var mutableClock: MutableClock

    @BeforeEach
    fun setup() {
        mutableClock = MutableClock(Clock.systemDefaultZone())
    }

    @Test
    fun `next watering date returns the next weekday date to water the plant`() {
        // SETUP
        val today = today()
        val plant = plant(waterDays = listOf(DayOfWeek.MONDAY))

        // ACTION
        val newPlant = plant.nextWateringDate(today)

        // ASSERTIONS
        assertThat(newPlant.wateringInfo.nextWateringDay.dayOfWeek).isEqualTo(DayOfWeek.MONDAY)
        assertThat(newPlant.wateringInfo.nextWateringDay.isAfter(today)).isTrue()
    }

    @Test
    fun `next watering date is today if watering weekday is today and watering time has not been reached yet`() {
        // SETUP
        val today = today()
        val wateringTime = today.plusMinutes(1).toLocalTime()
        val plant = plant(
            waterDays = listOf(today.dayOfWeek),
            wateringTime = wateringTime
        )

        // ACTION
        val newPlant = plant.nextWateringDate(today)

        // ASSERTIONS
        assertThat(newPlant.wateringInfo.nextWateringDay.dayOfWeek).isEqualTo(today.dayOfWeek)
        assertThat(newPlant.wateringInfo.nextWateringDay.dayOfMonth).isEqualTo(today.dayOfMonth)
        assertThat(newPlant.wateringInfo.nextWateringDay.hour).isEqualTo(wateringTime.hour)
        assertThat(newPlant.wateringInfo.nextWateringDay.minute).isEqualTo(wateringTime.minute)
        assertThat(newPlant.wateringInfo.nextWateringDay.isAfter(today)).isTrue()
    }

    @Test
    fun `next watering date is not today if watering weekday is today and watering time has been reached`() {
        // SETUP
        val today = today()
        val fourDaysAfterToday = today.plusDays(4)

        val wateringTime = today.minusMinutes(1).toLocalTime()
        val plant = plant(
            waterDays = listOf(today.dayOfWeek, fourDaysAfterToday.dayOfWeek),
            wateringTime = wateringTime
        )

        // ACTION
        val newPlant = plant.nextWateringDate(today)

        // ASSERTIONS
        assertThat(newPlant.wateringInfo.nextWateringDay.dayOfWeek).isEqualTo(fourDaysAfterToday.dayOfWeek)
        assertThat(newPlant.wateringInfo.nextWateringDay.hour).isEqualTo(wateringTime.hour)
        assertThat(newPlant.wateringInfo.nextWateringDay.minute).isEqualTo(wateringTime.minute)
        assertThat(newPlant.wateringInfo.nextWateringDay.isAfter(today)).isTrue()
    }

    @Test
    fun `watering a plant returns a new plant with its watered history updated with the current date and time`() = runTest {
        // SETUP
        val plant = plant()

        // ACTION
        assertThat(plant.wateringInfo.previousWaterDates).isEmpty()
        val newPlant = plant.water()

        // ASSERTIONS
        assertThat(newPlant.wateringInfo.previousWaterDates.size).isEqualTo(1)
        assertThat(newPlant.wateringInfo.previousWaterDates.first().dayOfMonth).isEqualTo(today().dayOfMonth)
        assertThat(newPlant.wateringInfo.previousWaterDates.first().toLocalTime().hour).isEqualTo(today().toLocalTime().hour)
        assertThat(newPlant.wateringInfo.previousWaterDates.first().toLocalTime().minute).isEqualTo(today().toLocalTime().minute)
    }

    @Test
    fun `undo watering a plant returns a new plant with the most recent watering date removed`() = runTest {
        // SETUP
        val today = today()
        val fiveDaysAfterToday = today.plusDays(5)
        val plant = plant(
            waterDays = listOf(today.dayOfWeek, fiveDaysAfterToday.dayOfWeek),
            wateringTime = today.toLocalTime().plusMinutes(1)
        )

        // ACTION
        val wateredTwicePlant = plant.run {
            val firstWater = water(mutableClock)
            mutableClock.advanceTimeBy((5 * 24).hours.toJavaDuration())
            firstWater.water(mutableClock)
        }
        val undoWateringPlant = wateredTwicePlant.undoPreviousWatering()

        // ASSERTIONS
        assertThat(wateredTwicePlant.wateringInfo.previousWaterDates.size).isEqualTo(2)
        assertThat(wateredTwicePlant.wateringInfo.previousWaterDates.last().dayOfWeek).isEqualTo(fiveDaysAfterToday.dayOfWeek)
        assertThat(undoWateringPlant.wateringInfo.previousWaterDates.size).isEqualTo(1)
        assertThat(undoWateringPlant.wateringInfo.previousWaterDates.last().dayOfWeek).isEqualTo(today.dayOfWeek)
    }

    @Test
    fun `plant is watered when the next watering date is the same day as the latest date watered`() = runTest {
        // SETUP
        val today = today()
        val plant = plant(
            waterDays = listOf(today.dayOfWeek),
            previousWateringDate = listOf(
                today.minusDays(6),
                today.minusDays(4)
            )
        )

        // ACTION
        val before = plant.hasBeenWatered
        val after = plant.water().hasBeenWatered

        // ASSERTIONS
        assertThat(before).isFalse()
        assertThat(after).isTrue()
    }

    @Test
    fun `plant needs water soon when next watering date is today`() = runTest {
        // SETUP
        val today = today()
        val plants = listOf<Plant>(
            plant(nextWateringDate = today.plusMinutes(1)),
            plant(nextWateringDate = today.plusDays(1)),
            plant(nextWateringDate = today.plusDays(1)),
            plant(nextWateringDate = today.plusDays(2)),
            plant(nextWateringDate = today.plusDays(3)),
        )

        // ACTION
        val plantsThatNeedsToBeWateredSoon = plants
            .map { it.nextWateringDate(today) }
            .filter { it.needsWaterSoon(today) }

        // ASSERTIONS
        assertThat(plantsThatNeedsToBeWateredSoon.size).isEqualTo(3)
        assertThat(plantsThatNeedsToBeWateredSoon.map { it.id }).contains(plants[0].id)
    }

    @Test
    fun `plant needs water soon when next watering date is tomorrow`() = runTest {
        // SETUP
        val today = today()
        val plants = (0..8).map {
            plant(nextWateringDate = today.plusDays(it.toLong()), name = "Plant #$it")
        }

        // ACTION
        val plantsThatNeedsToBeWateredSoon = plants
            .filter { it.needsWaterSoon(today.minusHours(1)) }

        // ASSERTIONS
        assertThat(plantsThatNeedsToBeWateredSoon.size).isEqualTo(2)
        assertThat(plantsThatNeedsToBeWateredSoon.map { it.details.name }).contains(plants[0].details.name)
        assertThat(plantsThatNeedsToBeWateredSoon.map { it.details.name}).contains(plants[1].details.name)
    }

    @Test
    fun `plant has been forgotten when the next watering date was yesterday`() = runTest {
        // SETUP
        val today = today()
        val plant = plant(nextWateringDate = today.minusDays(1))

        // ASSERTIONS
        assertThat(plant.forgotToWater(today())).isTrue()
    }

}