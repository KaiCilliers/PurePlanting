package com.sunrisekcdeveloper.pureplanting

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import org.junit.jupiter.api.Test
import java.time.DayOfWeek

class PlantTest {

    @Test
    fun `upcoming water date returns the next date to water plant`() {
        // SETUP
        val today = today(DayOfWeek.SATURDAY)
        val plant = plant(
            waterDays = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
        )

        // ACTION
         val nextWaterDate = plant.upcomingWateringDate()

        // ASSERTIONS
        assertThat(nextWaterDate.dayOfWeek).isEqualTo(DayOfWeek.MONDAY)
        assertThat(nextWaterDate).isGreaterThan(today)
    }

    @Test
    fun `upcoming water date returns today if water time has not been reached`() {
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
        val nextWaterDate = plant.upcomingWateringDate()

        // ASSERTIONS
        assertThat(nextWaterDate.dayOfWeek).isEqualTo(DayOfWeek.SATURDAY)
        assertThat(nextWaterDate.dayOfMonth).isEqualTo(today.dayOfMonth)
        assertThat(nextWaterDate.hour).isEqualTo(17)
    }

    // TODO: add parameterized tests with a methodsource annotation to pass multiple scenarios
    //  see https://stackoverflow.com/questions/57054115/use-pure-kotlin-function-as-junit5-methodsource
    @Test
    fun `upcoming water date returns future date if water time has been reached`() {
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
        val nextWaterDate = plant.upcomingWateringDate()

        // ASSERTIONS
        assertThat(nextWaterDate.dayOfWeek).isEqualTo(today.dayOfWeek)
        assertThat(nextWaterDate.hour).isEqualTo(plant.wateringInfo.atHour)
    }

}