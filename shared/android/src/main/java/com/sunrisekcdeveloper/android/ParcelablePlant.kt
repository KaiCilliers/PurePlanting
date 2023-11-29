package com.sunrisekcdeveloper.android

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

@Parcelize
data class ParcelablePlant(
    val id: String,
    val name: String,
    val size: String,
    val description: String,
    val imageSrcUri: String,
    val time: LocalTime,
    val days: List<DayOfWeek>,
    val amount: String,
    val datesWatered: List<LocalDateTime>,
    val userLastModifiedDate: LocalDateTime,
): Parcelable