package com.sunrisekcdeveloper.addEdit

import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.plant.domain.PlantDetails
import com.sunrisekcdeveloper.plant.domain.WateringInfo
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

fun plant(
    id: UUID = UUID.randomUUID(),
    waterDays: List<DayOfWeek> = listOf(DayOfWeek.MONDAY),
    atTime: LocalTime = LocalTime.now(),
    createdAt: LocalDateTime = LocalDateTime.now(),
    modifiedWaterDaysAt: LocalDateTime = LocalDateTime.now(),
): Plant {
    return Plant(
        id = id.toString(),
        details = PlantDetails(
            name = "Mitchel Cortez",
            size = "Medium",
            description = "fermentum",
            imageSrcUri = "",
        ),
        wateringInfo = WateringInfo(
            time = atTime,
            days = waterDays,
            amount = "240ml",
            daysLastModified = modifiedWaterDaysAt,
            history = listOf()
        ),
        createdAt = createdAt,
    )
}