package com.sunrisekcdeveloper.plant

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

data class WateringInfo(
    val time: LocalTime,
    val days: List<DayOfWeek>,
    val amount: String,
    val datesWatered: List<LocalDateTime> = emptyList()
)

