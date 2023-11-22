package com.sunrisekcdeveloper.pureplanting.features.component.plants

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
    val datesWatered: List<LocalDateTime> = emptyList()
) : Parcelable

