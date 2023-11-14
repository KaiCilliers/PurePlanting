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
    fun `when watered a new record is created in plants watering history`() {
        // SETUP
        
        // ACTION
        
        // ASSERTIONS
    
    }
    
    @Test
    fun `when watered a new record is created in plants watering history with today's date`() {
        // SETUP
        
        // ACTION
        
        // ASSERTIONS
    
    }
    
    @Test
    fun `needs water when water weekdays contains today`() {
        // SETUP
        
        // ACTION
        
        // ASSERTIONS
    
    }
    
    @Test
    fun `does not need water when water weekdays does not contain today`() {
        // SETUP
        
        // ACTION
        
        // ASSERTIONS
    
    }
    
    @Test
    fun `does not need water when water weekdays contains today and water record for today exist`() {
        // SETUP
        
        // ACTION
        
        // ASSERTIONS
    
    }
    
    @Test
    fun `needs water when water weekdays contains today and no water record exists for today`() {
        // SETUP
        
        // ACTION
        
        // ASSERTIONS
    
    }
    
    @Test
    fun `does not need water when water weekdays contains today and date modified is after plant time to water`() {
        // SETUP
        
        // ACTION
        
        // ASSERTIONS
    
    }
    
    @Test
    fun `forgot to water when water weekdays contain yesterday and no watering record exists on or after yesterday`() {
        // SETUP
        
        // ACTION
        
        // ASSERTIONS
    
    }
    
    @Test
    fun `forgot to water when plant is a week old and has no watering history`() {
        // SETUP
        
        // ACTION
        
        // ASSERTIONS
    
    }
    
    @Test
    fun `forgot to water when water weekdays contain a weekday between today and date last modified and has no watering history`() {
        // SETUP
        
        // ACTION
        
        // ASSERTIONS
    
    }
    
    @Test
    fun `not forgotten when water weekdays contains yesterday and latest watering record is today`() {
        // SETUP
        
        // ACTION
        
        // ASSERTIONS
    
    }
    
    @Test
    fun `not forgotten when water weekdays contains yesterday and latest watering record is after last date plant needed water`() {
        // SETUP
        
        // ACTION
        
        // ASSERTIONS
    
    }
    
    @Test
    fun `not forgotten when water weekdays contains yesterday and latest watering record is equal to the last date plant needed water`() {
        // SETUP
        
        // ACTION
        
        // ASSERTIONS
    
    }
    
    @Test
    fun `not forgotten after plant has been updated`() {
        // SETUP
        
        // ACTION
        
        // ASSERTIONS
    
    }

    /*
        Update plant on Tuesday to be watered on Mondays
        Today is Thursday --> plant is not forgotten
     */
    @Test
    fun `not forgotten when plant is updated before the first occurrence of a weekday it needs water`() {
        // SETUP

        // ACTION

        // ASSERTIONS

    }

    @Test
    fun `undo last watering reduces water history size by 1`() {
        // SETUP

        // ACTION

        // ASSERTIONS

    }

    @Test
    fun `undo last watering removes the latest watering record`() {
        // SETUP

        // ACTION

        // ASSERTIONS

    }

    @Test
    fun `is watered when latest watering record is today`() {
        // SETUP

        // ACTION

        // ASSERTIONS

    }

    @Test
    fun `is not watered when latest watering record is not today`() {
        // SETUP

        // ACTION

        // ASSERTIONS

    }

    @Test
    fun `is watered when not forgotten to water and has watering record`() {
        // SETUP

        // ACTION

        // ASSERTIONS

    }

}