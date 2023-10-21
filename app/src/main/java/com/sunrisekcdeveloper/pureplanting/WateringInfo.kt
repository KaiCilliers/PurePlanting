package com.sunrisekcdeveloper.pureplanting

import java.time.DayOfWeek

data class WateringInfo(
    val atHour: Int,
    val days: List<DayOfWeek>,
    val amount: String,
)