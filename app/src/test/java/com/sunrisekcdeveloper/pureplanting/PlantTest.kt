package com.sunrisekcdeveloper.pureplanting

import assertk.assertThat
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

}