package com.sunrisekcdeveloper.pureplanting.features.presentation.plants

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.sunrisekcdeveloper.shared_test.MutableClock
import com.sunrisekcdeveloper.shared_test.earlyMorning
import com.sunrisekcdeveloper.shared_test.plant
import com.sunrisekcdeveloper.shared_test.plantNeedsWater
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
        
        // ASSERTIONS
        assertThat(plant.wateringInfo.datesWatered).isEmpty()
        assertThat(newPlant.wateringInfo.datesWatered).isNotEmpty()
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
        assertThat(plant.needsWater(today)).isTrue()
    }
    
    @Test
    fun `does not need water when water weekdays does not contain today`() {
        // SETUP
        val today = today()
        val tomorrow = today.plusDays(1)
        val plant = plantNeedsWater(tomorrow)

        // ASSERTIONS
        assertThat(plant.needsWater(today)).isFalse()
    }
    
    @Test
    fun `does not need water when water weekdays contains today and water record for today exist`() {
        // SETUP
        val today = today()
        val plant = plantNeedsWater(today)

        // ACTION
        val wateredPlant = plant.water()
        
        // ASSERTIONS
        assertThat(wateredPlant.needsWater(today)).isFalse()
    }
    
    @Test
    fun `needs water when water weekdays contains today and no water record exists for today`() {
        // SETUP
        val today = today()
        val plant = plantNeedsWater(earlyMorning(), modifiedAt = earlyMorning().minusHours(1))

        // ASSERTIONS
        assertThat(plant.needsWater(today)).isTrue()
    }
    
    @Test
    fun `does not need water when water weekdays contains today and date modified is after plant time to water`() {
        // SETUP
        val today = today()
        val anHourAgo = today.minusHours(1)
        val plant = plantNeedsWater(anHourAgo)
        
        // ASSERTIONS
        assertThat(plant.wateringInfo.time.hour).isEqualTo(anHourAgo.hour)
        assertThat(plant.wateringInfo.time.minute).isEqualTo(anHourAgo.minute)
        assertThat(plant.needsWater(today)).isFalse()
    }
    
    @Test
    fun `forgot to water when water weekdays contain yesterday and no watering record exists on or after yesterday`() {
        // SETUP
        val today = today()
        val yesterday = today.minusDays(1)
        val plant = plant(
            waterDays = listOf(yesterday.dayOfWeek),
            modifiedAt = yesterday.minusDays(1)
        )

        // ASSERTIONS
        assertThat(plant.forgotToWater(today)).isTrue()
    }
    
    @Test
    fun `forgot to water when plant is a older than a week and has no watering history`() {
        // SETUP
        val today = today()
        val yesterday = today.minusDays(1)
        val weekOldPlant = plant(
            waterDays = listOf(yesterday.dayOfWeek),
            modifiedAt = today.minusDays(30)
        )
        
        // ACTION
        val forgotToWater = weekOldPlant.forgotToWater(today)
        
        // ASSERTIONS
        assertThat(forgotToWater).isTrue()
    }

    @Test
    fun `not forgotten when plant is upcoming`() {
        // SETUP
        val today = today()
        val weekOldPlant = plant(
            waterDays = listOf(today.dayOfWeek),
            modifiedAt = today.minusDays(7)
        )

        // ACTION
        val forgotToWater = weekOldPlant.forgotToWater(today)
        val needsWater = weekOldPlant.needsWater(today)

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
            modifiedAt = today.minusDays(4)
        )
        
        // ACTION
        val forgotToWater = plant.forgotToWater(today)
        
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
            modifiedAt = yesterday.minusDays(1)
        )

        // ACTION
        val wateredPlant = plant.water()

        // ASSERTIONS
        assertThat(wateredPlant.forgotToWater(today)).isFalse()
    }
    
    @Test
    fun `not forgotten when water weekdays contains yesterday and latest watering record is yesterday`() {
        // SETUP
        val today = today()
        val yesterday = today.minusDays(1)
        val plant = plant(
            waterDays = listOf(yesterday.dayOfWeek),
            modifiedAt = yesterday.minusDays(1)
        )
        mutableClock.reverseTimeBy(1.days.toJavaDuration())
        
        // ACTION
        val forgotToWater = plant.water(mutableClock).forgotToWater(today)
        
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
            modifiedAt = yesterday.minusDays(1)
        )

        // ACTION
        val userUpdatedPlant = forgotToWaterPlant.copy(userLastModifiedDate = today)
        
        // ASSERTIONS
        assertThat(forgotToWaterPlant.forgotToWater(today)).isTrue()
        assertThat(userUpdatedPlant.forgotToWater(today)).isFalse()
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
            modifiedAt = yesterday.minusDays(7)
        )

        // ACTION
        val updatedPlant = plant.copy(
            wateringInfo = plant.wateringInfo.copy(
                days = listOf(fourDaysAgo.dayOfWeek)
            ),
            userLastModifiedDate = fourDaysAgo.plusDays(1)
        )

        // ASSERTIONS
        assertThat(plant.forgotToWater(today)).isTrue()
        assertThat(updatedPlant.forgotToWater(today)).isFalse()
    }

    @Test
    fun `undo last watering reduces water history size by 1`() {
        // SETUP
        val plant = plant()

        // ACTION
        val wateredPlant = plant.water().water()
        val unWateredPlant = wateredPlant.undoLastWatering()

        // ASSERTIONS
        assertThat(wateredPlant.wateringInfo.datesWatered.size).isEqualTo(2)
        assertThat(unWateredPlant.wateringInfo.datesWatered.size).isEqualTo(1)
    }

    @Test
    fun `undo last watering removes the latest watering record`() {
        // SETUP
        val plant = plant()

        // ACTION
        val wateredPlant = plant.water().water()
        val wateredPlantLatestWaterDate = wateredPlant.dateLastWatered
        val unWateredPlantLatestWaterDate = wateredPlant.undoLastWatering().dateLastWatered

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
        assertThat(wateredPlant.isWatered(today)).isTrue()
    }

    @Test
    fun `is not watered when latest watering record is not today`() {
        // SETUP
        val today = today()
        val plant = plant()
        mutableClock.reverseTimeBy(4.days.toJavaDuration())

        // ACTION
        val wateredPlant = plant.water(mutableClock)

        // ASSERTIONS
        assertThat(wateredPlant.isWatered(today)).isFalse()
    }

    @Test
    fun `is not watered when there is no watering history`() {
        // SETUP
        val today = today()
        val plant = plant()

        // ACTION
        val isWatered = plant.isWatered(today)

        // ASSERTIONS
        assertThat(isWatered).isFalse()
    }

    @Test
    fun `is watered when not forgotten to water and has watering record`() {
        // SETUP
        val today = today()
        val forgotToWaterPlant = plant(
            waterDays = listOf(today.minusDays(2).dayOfWeek),
            modifiedAt = today.minusDays(7)
        )
        mutableClock.reverseTimeBy(2.days.toJavaDuration())

        // ACTION
        val wateredTwoDaysAgoPlant = forgotToWaterPlant.water(mutableClock)

        // ASSERTIONS
        assertThat(wateredTwoDaysAgoPlant.isWatered(today)).isTrue()
    }

}