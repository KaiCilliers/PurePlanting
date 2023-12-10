package com.sunrisekcdeveloper.plant.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

@Parcelize
data class WateringInfo(
    val time: LocalTime,
    val days: List<DayOfWeek>,
    val amount: String,
    val daysLastModified: LocalDateTime = LocalDateTime.now(),
    val history: List<LocalDateTime> = emptyList(),
): Parcelable

