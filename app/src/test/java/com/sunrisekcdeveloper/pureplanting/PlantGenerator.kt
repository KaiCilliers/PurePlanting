package com.sunrisekcdeveloper.pureplanting

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

fun plant(
    id: UUID = UUID.randomUUID()
): Plant {
    return Plant(
        id = id,
        name = "Mitchel Cortez",
        size = "pharetra",
        description = "fermentum",
        imageSrcUri = "",
        wateringInfo = WateringInfo(
            atHour = LocalTime.now(),
            dayOfTheWeek = DayOfWeek.MONDAY,
            amount = "dictumst"
        )
    )
}