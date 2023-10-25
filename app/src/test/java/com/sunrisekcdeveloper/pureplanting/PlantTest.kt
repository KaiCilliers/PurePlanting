package com.sunrisekcdeveloper.pureplanting

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isGreaterThan
import assertk.assertions.isTrue
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Stack


class PlantTest {

    @Test
    fun `next watering date function returns plant model with the next date to water plant`() {
        // SETUP
        val today = today(DayOfWeek.SATURDAY)
        val plant = plant(
            waterDays = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
        )

        // ACTION
        val newPlant = plant.nextWateringDate(today)

        // ASSERTIONS
        assertThat(newPlant.wateringInfo.nextWateringDay.dayOfWeek).isEqualTo(DayOfWeek.MONDAY)
        assertThat(newPlant.wateringInfo.nextWateringDay).isGreaterThan(today)
    }

    @Test
    fun `next watering date function returns plant model with next watering date as today if water time has not been reached`() {
        // SETUP
        val today = today(
            dayOfWeek = DayOfWeek.SATURDAY,
            hour = 16
        )
        val plant = plant(
            waterDays = listOf(DayOfWeek.SATURDAY),
            wateringHour = 17
        )

        // ACTION
        val newPlant = plant.nextWateringDate(today)

        // ASSERTIONS
        assertThat(newPlant.wateringInfo.nextWateringDay.dayOfWeek).isEqualTo(DayOfWeek.SATURDAY)
        assertThat(newPlant.wateringInfo.nextWateringDay.dayOfMonth).isEqualTo(today.dayOfMonth)
        assertThat(newPlant.wateringInfo.nextWateringDay.hour).isEqualTo(17)
    }

    @Test
    fun `next watering date function returns plant model with watering date after today if water time has been reached`() {
        // SETUP
        val today = today(
            dayOfWeek = DayOfWeek.SATURDAY,
            hour = 17
        )
        val plant = plant(
            waterDays = listOf(DayOfWeek.SATURDAY, DayOfWeek.TUESDAY),
            wateringHour = 17
        )

        // ACTION
        val newPlant = plant.nextWateringDate(today)

        // ASSERTIONS
        assertThat(newPlant.wateringInfo.nextWateringDay.dayOfWeek).isEqualTo(DayOfWeek.TUESDAY)
        assertThat(newPlant.wateringInfo.nextWateringDay.hour).isEqualTo(plant.wateringInfo.atHour)
    }

    @Test
    fun `plant is watered, return new model with last watered dates list updated`() = runTest {
        // SETUP
        val plant = plant()

        // ACTION
        assertThat(plant.wateringInfo.previousWaterDates).isEmpty()
        val newPlant = plant.water()

        // ASSERTIONS
        assertThat(newPlant.wateringInfo.previousWaterDates.size).isEqualTo(1)
    }

    @Test
    fun `plant is un-watered, return model with the most recent watering date removed`() = runTest {
        // SETUP
        val plant = plant()

        // ACTION
        var newPlant = plant.water()
        newPlant = newPlant.water()
        assertThat(plant.wateringInfo.previousWaterDates.size).isEqualTo(2)

        // ASSERTIONS
        assertThat(newPlant.undoPreviousWatering().wateringInfo.previousWaterDates.size).isEqualTo(1)
    }

    @Test
    fun `plant is considered watered when the next watering date is the same day as most recent watering day`() = runTest {
        // SETUP
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val today = LocalDateTime.parse("2023-06-09 12:30", formatter)
        val plant = plant(
            waterDays = listOf(today.dayOfWeek),
            previousWateringDate = Stack<LocalDateTime>().apply {
                push(LocalDateTime.parse("2023-06-03 12:30", formatter))
                push(LocalDateTime.parse("2023-06-05 12:30", formatter))
            }
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
        val today = today(
            dayOfWeek = DayOfWeek.MONDAY,
            hour = 0
        )
        val plants = listOf(
            plant(waterDays = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), wateringHour = 12),
            plant(waterDays = listOf(DayOfWeek.SUNDAY, DayOfWeek.TUESDAY), wateringHour = 12),
            plant(waterDays = listOf(DayOfWeek.WEDNESDAY, DayOfWeek.MONDAY), wateringHour = 12),
            plant(waterDays = listOf(DayOfWeek.THURSDAY, DayOfWeek.SATURDAY), wateringHour = 12),
            plant(waterDays = listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), wateringHour = 12),
        )

        // ACTION
        val plantsThatNeedsToBeWateredSoon = plants
            .map { it.nextWateringDate(today) }
            .filter { it.needsWaterSoon(today) }

        // ASSERTIONS
        assertThat(plantsThatNeedsToBeWateredSoon.map { it.id }).contains(plants[0].id)
        assertThat(plantsThatNeedsToBeWateredSoon.map { it.id }).contains(plants[2].id)
        assertThat(plantsThatNeedsToBeWateredSoon.size).isEqualTo(4)
    }

    @Test
    fun `plant needs water soon when next watering date is tomorrow`() = runTest {
        // SETUP
        val today = today(
            dayOfWeek = DayOfWeek.MONDAY,
            hour = 0
        )
        val plants = listOf(
            plant(waterDays = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), wateringHour = 12),
            plant(waterDays = listOf(DayOfWeek.SUNDAY, DayOfWeek.TUESDAY), wateringHour = 12),
            plant(waterDays = listOf(DayOfWeek.WEDNESDAY, DayOfWeek.MONDAY), wateringHour = 12),
            plant(waterDays = listOf(DayOfWeek.THURSDAY, DayOfWeek.SATURDAY), wateringHour = 12),
            plant(waterDays = listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), wateringHour = 12),
            plant(waterDays = listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), wateringHour = 12),
            plant(waterDays = listOf(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY), wateringHour = 12),
            plant(waterDays = listOf(DayOfWeek.FRIDAY, DayOfWeek.THURSDAY), wateringHour = 12),
        )

        // ACTION
        val plantsThatNeedsToBeWateredSoon = plants
            .map { it.nextWateringDate(today) }
            .filter { it.needsWaterSoon(today) }

        // ASSERTIONS
        assertThat(plantsThatNeedsToBeWateredSoon.map { it.id }).contains(plants[1].id)
        assertThat(plantsThatNeedsToBeWateredSoon.map { it.id }).contains(plants[4].id)
        assertThat(plantsThatNeedsToBeWateredSoon.size).isEqualTo(5)
    }

    @Test
    fun `plant has been forgotten when the next watering date is the previous day compared to today`() = runTest {
        // SETUP
        val today = today(DayOfWeek.THURSDAY)
        val plant = plant(
            waterDays = listOf(DayOfWeek.FRIDAY, DayOfWeek.SUNDAY)
        ).nextWateringDate(today)

        // ACTION
        val newPlant = plant
            .water()
            .nextWateringDate(today)

        // ASSERTIONS
        assertThat(newPlant.hasBeenWatered).isFalse()
        assertThat(newPlant.wateringInfo.nextWateringDay.dayOfWeek).isEqualTo(DayOfWeek.FRIDAY)
        assertThat(newPlant.wateringInfo.previousWaterDates.size).isEqualTo(1)
        assertThat(newPlant.forgotToWater(today(DayOfWeek.SATURDAY))).isTrue()
    }

}