package com.sunrisekcdeveloper.pureplanting

import java.time.DayOfWeek
import java.time.LocalTime

data class WateringInfo(
    val atHour: LocalTime,
    val dayOfTheWeek: DayOfWeek,
    val amount: String,
)