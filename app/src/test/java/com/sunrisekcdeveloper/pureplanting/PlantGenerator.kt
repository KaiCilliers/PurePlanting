package com.sunrisekcdeveloper.pureplanting

import java.time.DayOfWeek
import java.util.UUID

fun plant(
    id: UUID = UUID.randomUUID(),
    waterDays: List<DayOfWeek> = listOf(DayOfWeek.MONDAY),
    wateringHour: Int = 0
): Plant {
    return Plant(
        id = id,
        name = "Mitchel Cortez",
        size = "pharetra",
        description = "fermentum",
        imageSrcUri = "",
        wateringInfo = WateringInfo(
            atHour = wateringHour,
            days = waterDays,
            amount = "dictumst"
        )
    )
}