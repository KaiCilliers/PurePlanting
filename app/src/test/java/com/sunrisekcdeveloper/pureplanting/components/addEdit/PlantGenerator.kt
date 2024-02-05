package com.sunrisekcdeveloper.home.com.sunrisekcdeveloper.pureplanting.components.addEdit

import com.sunrisekcdeveloper.pureplanting.business.plant.Plant
import com.sunrisekcdeveloper.pureplanting.business.plant.PlantDetails
import com.sunrisekcdeveloper.pureplanting.business.plant.WateringInfo
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
            lastModifiedWateringDays = modifiedWaterDaysAt,
            history = listOf()
        ),
        createdAt = createdAt,
    )
}