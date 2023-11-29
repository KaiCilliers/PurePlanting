package com.sunrisekcdeveloper.plant

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs

fun LocalDateTime.getDaysBetween(to: LocalDateTime?): Long =
    abs(ChronoUnit.DAYS.between(this, to))

fun LocalDateTime.getDayOfWeeksBetween(other: LocalDateTime): List<DayOfWeek> {
    val weekdays = mutableSetOf<DayOfWeek>()
    val (earlierDate, laterDate) = if (this.isBefore(other)) {
        this.toLocalDate() to other.toLocalDate()
    } else other.toLocalDate() to this.toLocalDate()

    return if (laterDate.isAfter(earlierDate)) {
        weekdays.add(earlierDate.dayOfWeek)
        var _laterDate = laterDate.minusDays(1)

        while (_laterDate.isAfter(earlierDate)) {
            weekdays.add(_laterDate.dayOfWeek)
            _laterDate = _laterDate.minusDays(1)
        }
        weekdays.toList()
    } else emptyList()
}