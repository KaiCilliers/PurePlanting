package com.sunrisekcdeveloper.pureplanting.util

import android.text.format.DateUtils
import java.lang.Math.abs
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

object DateFormat {
    // src https://medium.com/bugless/lost-in-java-time-with-android-12822a98b194
    const val ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'"
}

fun String.toLocalDateTime(pattern: String = DateFormat.ISO_8601): LocalDateTime? = try {
    LocalDateTime.parse(this, DateTimeFormatter.ofPattern(pattern))
} catch (exception: Exception) {
    null
}

fun LocalDateTime.format(pattern: String = DateFormat.ISO_8601): String? = try {
    DateTimeFormatter.ofPattern(pattern).format(this)
} catch (exception: Exception) {
    null
}

fun LocalDateTime.getRelativeTimeSpanString(now: LocalDateTime?): CharSequence? =
    DateUtils.getRelativeTimeSpanString(
        getEpochMilli(),
        (now ?: LocalDateTime.now()).getEpochMilli(),
        DateUtils.MINUTE_IN_MILLIS,
        DateUtils.FORMAT_ABBREV_TIME
    )

fun LocalDateTime.getEpochMilli(): Long =
    atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

fun LocalDateTime.getDaysTo(to: LocalDateTime?): Long =
    ChronoUnit.DAYS.between(this, to)

fun LocalDateTime.getDaysBetween(to: LocalDateTime?): Long =
    abs(ChronoUnit.DAYS.between(this, to))

fun LocalDateTime.nextDayOfTheWeek(dayOfWeek: DayOfWeek): LocalDateTime =
    this.plusHours(1)
        .with(TemporalAdjusters.next(dayOfWeek))
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()

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