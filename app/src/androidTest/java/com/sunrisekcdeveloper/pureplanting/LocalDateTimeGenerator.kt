package com.sunrisekcdeveloper.pureplanting

import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

fun today(clock: Clock = Clock.systemDefaultZone(),): LocalDateTime = LocalDateTime.now(clock)

fun earlyMorning(clock: Clock = Clock.systemDefaultZone(),): LocalDateTime = LocalDateTime.now(clock).withHour(8).withMinute(0)

fun today(
    dayOfWeek: DayOfWeek,
    hour: Int = 0,
    clock: Clock = Clock.systemDefaultZone(),
): LocalDateTime {
    return if (now(clock).dayOfWeek != dayOfWeek) {
        now(clock)
            .with(TemporalAdjusters.next(dayOfWeek))
            .with(LocalTime.of(hour, 0))
    } else now(clock)
}

fun now(clock: Clock = Clock.systemDefaultZone()): LocalDateTime = LocalDateTime.now(clock)