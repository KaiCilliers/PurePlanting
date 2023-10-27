package com.sunrisekcdeveloper.pureplanting.features.presentation.plants

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.Stack

data class WateringInfo(
    val atHour: Int,
    val days: List<DayOfWeek>,
    val amount: String,
    val nextWateringDay: LocalDateTime,
    val previousWaterDates: Stack<LocalDateTime> = Stack()
)

data class PlantDetails(
    val name: String,
    val size: String,
    val description: String,
    val imageSrcUri: String,
)