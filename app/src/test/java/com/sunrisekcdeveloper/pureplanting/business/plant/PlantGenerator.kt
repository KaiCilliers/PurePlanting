package com.sunrisekcdeveloper.home.com.sunrisekcdeveloper.pureplanting.business.plant

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
): com.sunrisekcdeveloper.pureplanting.business.plant.Plant {
    return com.sunrisekcdeveloper.pureplanting.business.plant.Plant(
        id = id.toString(),
        details = com.sunrisekcdeveloper.pureplanting.business.plant.PlantDetails(
            name = "Mitchel Cortez",
            size = "Medium",
            description = "fermentum",
            imageSrcUri = "",
        ),
        wateringInfo = com.sunrisekcdeveloper.pureplanting.business.plant.WateringInfo(
            time = atTime,
            days = waterDays,
            amount = "240ml",
            lastModifiedWateringDays = modifiedWaterDaysAt,
            history = listOf()
        ),
        createdAt = createdAt,
    )
}

fun plantForgotten(date: LocalDateTime = LocalDateTime.now()): com.sunrisekcdeveloper.pureplanting.business.plant.Plant {
    return plant(
        waterDays = listOf(date.minusDays(1).dayOfWeek),
        atTime = LocalTime.of(date.hour, date.minute, 0),
        createdAt = date.minusDays(2),
        modifiedWaterDaysAt = date.minusDays(2)
    )
}

fun plantNeedsWater(date: LocalDateTime, modifiedAt: LocalDateTime = LocalDateTime.now()): com.sunrisekcdeveloper.pureplanting.business.plant.Plant {
    return plant(
        waterDays = listOf(date.dayOfWeek),
        atTime = LocalTime.of(date.hour, date.minute, 0),
        createdAt = modifiedAt
    )
}

fun plantNeedsWaterNow(date: LocalDateTime = LocalDateTime.now()): com.sunrisekcdeveloper.pureplanting.business.plant.Plant {
    return plant(
        waterDays = listOf(date.dayOfWeek),
        atTime = date.toLocalTime(),
        createdAt = date.minusHours(1),
    )
}