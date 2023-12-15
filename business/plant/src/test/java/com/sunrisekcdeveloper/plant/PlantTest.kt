package com.sunrisekcdeveloper.plant

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.sunrisekcdeveloper.shared_test.MutableClock
import com.sunrisekcdeveloper.shared_test.earlyMorning
import com.sunrisekcdeveloper.shared_test.today
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.toJavaDuration


class PlantTest {

    private lateinit var mutableClock: MutableClock

    @BeforeEach
    fun setup() {
        mutableClock = MutableClock(Clock.systemDefaultZone())
    }

    @Test
    fun `when watered a new record is created in plants watering history`() {
        // SETUP
        val plant = plant()

        // ACTION
        val newPlant = plant.water()

        // ASSERTIONS )
        assertThat(plant.wateringInfo.history).isEmpty()
        assertThat(newPlant.wateringInfo.history).isNotEmpty()
    }

    @Test
    fun `when watered a new record is created in plants watering history with today's date`() {
        // SETUP
        val plant = plant()
        val today = today()

        // ACTION
        val newPlant = plant.water()
        val latestWateringRecord = newPlant.dateLastWatered

        // ASSERTIONS
        assertThat(latestWateringRecord).isNotNull()
        assertThat(latestWateringRecord!!.dayOfWeek).isEqualTo(today.dayOfWeek)
        assertThat(latestWateringRecord.dayOfMonth).isEqualTo(today.dayOfMonth)
        assertThat(latestWateringRecord.dayOfYear).isEqualTo(today.dayOfYear)
        assertThat(latestWateringRecord.hour).isEqualTo(today.hour)
        assertThat(latestWateringRecord.minute).isEqualTo(today.minute)
    }

    @Test
    fun `needs water when water weekdays contains today`() {
        // SETUP
        val today = today()
        val plant = plantNeedsWater(earlyMorning(), modifiedAt = earlyMorning().minusHours(1))

        // ASSERTIONS
        assertThat(plant.needsWaterToday(today)).isTrue()
    }

    @Test
    fun `does not need water when water weekdays does not contain today`() {
        // SETUP
        val today = today()
        val tomorrow = today.plusDays(1)
        val plant = plantNeedsWater(tomorrow)

        // ASSERTIONS
        assertThat(plant.needsWaterToday(today)).isFalse()
    }

    @Test
    fun `does not need water when water weekdays contains today and water record for today exist`() {
        // SETUP
        val today = today()
        val plant = plantNeedsWater(today)

        // ACTION
        val wateredPlant = plant.water()

        // ASSERTIONS
        assertThat(wateredPlant.needsWaterToday(today)).isFalse()
    }

    @Test
    fun `needs water when water weekdays contains today and no water record exists for today`() {
        // SETUP
        val today = today()
        val plant = plantNeedsWater(earlyMorning(), modifiedAt = earlyMorning().minusHours(1))

        // ASSERTIONS
        assertThat(plant.needsWaterToday(today)).isTrue()
    }

    @Test
    fun `forgot to water when water weekdays contain yesterday and no watering record exists on or after yesterday`() {
        // SETUP
        val today = today()
        val yesterday = today.minusDays(1)
        val plant = plant(
            waterDays = listOf(yesterday.dayOfWeek),
            modifiedWaterDaysAt = yesterday.minusDays(1)
        )

        // ASSERTIONS
        assertThat(plant.missedLatestWateringDate(today)).isTrue()
    }

    @Test
    fun `forgot to water when plant is a older than a week and has no watering history`() {
        // SETUP
        val today = today()
        val yesterday = today.minusDays(1)
        val weekOldPlant = plant(
            waterDays = listOf(yesterday.dayOfWeek),
            modifiedWaterDaysAt = today.minusDays(30)
        )

        // ACTION
        val forgotToWater = weekOldPlant.missedLatestWateringDate(today)

        // ASSERTIONS
        assertThat(forgotToWater).isTrue()
    }

    @Test
    fun `not forgotten when plant is upcoming`() {
        // SETUP
        val today = today()
        val weekOldPlant = plant(
            waterDays = listOf(today.dayOfWeek),
            createdAt = today.minusDays(7)
        )

        // ACTION
        val forgotToWater = weekOldPlant.missedLatestWateringDate(today)
        val needsWater = weekOldPlant.needsWaterToday(today)

        // ASSERTIONS
        assertThat(needsWater).isTrue()
        assertThat(forgotToWater).isFalse()
    }

    @Test
    fun `forgot to water when water weekdays contain a weekday between today and date last modified and has no watering history`() {
        // SETUP
        val today = today()
        val plant = plant(
            waterDays = listOf(today.minusDays(2).dayOfWeek),
            modifiedWaterDaysAt = today.minusDays(4)
        )

        // ACTION
        val forgotToWater = plant.missedLatestWateringDate(today)

        // ASSERTIONS
        assertThat(forgotToWater).isTrue()

    }

    @Test
    fun `not forgotten when water weekdays contains yesterday and latest watering record is today`() {
        // SETUP
        val today = today()
        val yesterday = today.minusDays(1)
        val plant = plant(
            waterDays = listOf(yesterday.dayOfWeek),
            createdAt = yesterday.minusDays(1)
        )

        // ACTION
        val wateredPlant = plant.water()

        // ASSERTIONS
        assertThat(wateredPlant.missedLatestWateringDate(today)).isFalse()
    }

    @Test
    fun `not forgotten when water weekdays contains yesterday and latest watering record is yesterday`() {
        // SETUP
        val today = today()
        val yesterday = today.minusDays(1)
        val plant = plant(
            waterDays = listOf(yesterday.dayOfWeek),
            createdAt = yesterday.minusDays(1)
        )
        mutableClock.reverseTimeBy(1.days.toJavaDuration())

        // ACTION
        val forgotToWater = plant.water(mutableClock).missedLatestWateringDate(today)

        // ASSERTIONS
        assertThat(forgotToWater).isFalse()
    }

    @Test
    fun `not forgotten after plant has been updated`() {
        // SETUP
        val today = today()
        val yesterday = today.minusDays(1)
        val forgotToWaterPlant = plant(
            waterDays = listOf(yesterday.dayOfWeek),
            modifiedWaterDaysAt = yesterday.minusDays(1)
        )

        // ACTION
        val userUpdatedPlant = forgotToWaterPlant.copy(wateringInfo = forgotToWaterPlant.wateringInfo.copy(
            daysLastModified = today
        ))

        // ASSERTIONS
        assertThat(forgotToWaterPlant.missedLatestWateringDate(today)).isTrue()
        assertThat(userUpdatedPlant.missedLatestWateringDate(today)).isFalse()
    }

    /*
        Update plant on Tuesday to be watered on Mondays
        Today is Thursday --> plant is not forgotten
     */
    @Test
    fun `not forgotten when plant's weekdays is modified on a weekday after the weekday it needs water`() {
        // SETUP
        val today = today()
        val yesterday = today.minusDays(1)
        val fourDaysAgo = today.minusDays(4)
        val plant = plant(
            waterDays = listOf(yesterday.dayOfWeek),
            modifiedWaterDaysAt = yesterday.minusDays(7)
        )

        // ACTION
        val updatedPlant = plant.copy(
            wateringInfo = plant.wateringInfo.copy(
                days = listOf(fourDaysAgo.dayOfWeek),
                daysLastModified = fourDaysAgo.plusDays(1)
            ),
        )

        // ASSERTIONS
        assertThat(plant.missedLatestWateringDate(today)).isTrue()
        assertThat(updatedPlant.missedLatestWateringDate(today)).isFalse()
    }

    @Test
    fun `undo last watering reduces water history size by 1`() {
        // SETUP
        val plant = plant()

        // ACTION
        val wateredPlant = plant.water().water()
        val unWateredPlant = wateredPlant.undoWatering()

        // ASSERTIONS
        assertThat(wateredPlant.wateringInfo.history.size).isEqualTo(2)
        assertThat(unWateredPlant.wateringInfo.history.size).isEqualTo(1)
    }

    @Test
    fun `undo last watering removes the latest watering record`() {
        // SETUP
        val plant = plant()

        // ACTION
        val wateredPlant = plant.water().water()
        val wateredPlantLatestWaterDate = wateredPlant.dateLastWatered
        val unWateredPlantLatestWaterDate = wateredPlant.undoWatering().dateLastWatered

        // ASSERTIONS
        assertThat(wateredPlantLatestWaterDate!!.isAfter(unWateredPlantLatestWaterDate))
    }

    @Test
    fun `is watered when latest watering record is today`() {
        // SETUP
        val today = today()
        val plant = plant()

        // ACTION
        val wateredPlant = plant.water()

        // ASSERTIONS
        assertThat(wateredPlant.needsWaterToday(today)).isFalse()
    }

    @Test
    fun `is watered when not forgotten to water and has watering record`() {
        // SETUP
        val today = today()
        val forgotToWaterPlant = plant(
            waterDays = listOf(today.minusDays(2).dayOfWeek),
            createdAt = today.minusDays(7)
        )
        mutableClock.reverseTimeBy(2.days.toJavaDuration())

        // ACTION
        val wateredTwoDaysAgoPlant = forgotToWaterPlant.water(mutableClock)

        // ASSERTIONS
        assertThat(wateredTwoDaysAgoPlant.needsWaterToday(today)).isFalse()
    }

}