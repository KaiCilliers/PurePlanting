package com.sunrisekcdeveloper.pureplanting.features.component.plants

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek
import java.time.LocalDateTime

@Parcelize
data class WateringInfo(
    val atHour: Int,
    val atMin: Int,
    val days: List<DayOfWeek>,
    val amount: String,
    val nextWateringDay: LocalDateTime,
    val previousWaterDates: List<LocalDateTime> = emptyList()
) : Parcelable

@Parcelize
data class PlantDetails(
    val name: String,
    val size: String,
    val description: String,
    val imageSrcUri: String,
) : Parcelable