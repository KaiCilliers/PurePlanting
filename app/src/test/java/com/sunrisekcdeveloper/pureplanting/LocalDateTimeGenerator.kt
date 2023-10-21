package com.sunrisekcdeveloper.pureplanting

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

fun today(
    dayOfWeek: DayOfWeek,
    hour: Int = 0,
): LocalDateTime {
    return now().apply {
        if (this.dayOfWeek != dayOfWeek) {
            with(TemporalAdjusters.next(dayOfWeek))
            with(LocalTime.of(hour, 0))
        }
    }
}

fun now(): LocalDateTime = LocalDateTime.now()