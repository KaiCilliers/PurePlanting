package com.sunrisekcdeveloper.pureplanting.util

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsAll
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import com.sunrisekcdeveloper.shared_test.today
import org.junit.jupiter.api.Test
import java.time.DayOfWeek

class LocalDateTimeExtTest {

    @Test
    fun `weekdays between does not return latest date if there is less than week difference`() {
        // SETUP
        val today = today()
        val fourDaysAgo = today.minusDays(4)

        // ACTION
        val weekdays = today.getDayOfWeeksBetween(fourDaysAgo)

        // ASSERTIONS
        assertThat(weekdays).contains(fourDaysAgo.dayOfWeek)
        assertThat(weekdays).contains(today.minusDays(3).dayOfWeek)
        assertThat(weekdays).contains(today.minusDays(2).dayOfWeek)
        assertThat(weekdays).contains(today.minusDays(1).dayOfWeek)
    }

    @Test
    fun `weekdays between returns all weekdays if there a week or more difference in dates`() {
        // SETUP
        val today = today()
        val weekAgo = today.minusDays(7)

        // ACTION
        val weekdays = today.getDayOfWeeksBetween(weekAgo)

        // ASSERTIONS
        assertThat(weekdays).containsAll(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY,
        )
    }

    @Test
    fun `using the same dates returns same result regardless of order used`() {
        val today = today()
        val fourDaysAgo = today.minusDays(4)

        // ACTION
        val weekdays = today.getDayOfWeeksBetween(fourDaysAgo)
        val weekdaysReversed = fourDaysAgo.getDayOfWeeksBetween(today)

        // ASSERTIONS
        assertThat(weekdays).containsExactly(*weekdaysReversed.toTypedArray())
    }

    @Test
    fun `returns empty list if both dates are the same`() {
        val today = today()

        // ACTION
        val weekdays = today.getDayOfWeeksBetween(today)

        // ASSERTIONS
        assertThat(weekdays).isEmpty()
    }

}