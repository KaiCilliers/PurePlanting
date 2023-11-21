package com.sunrisekcdeveloper.shared_test

import com.sunrisekcdeveloper.pureplanting.features.component.plants.Plant
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantDetails
import com.sunrisekcdeveloper.pureplanting.features.component.plants.WateringInfo
import com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant.components.PlantSize
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

fun plant(
    id: UUID = UUID.randomUUID(),
    waterDays: List<DayOfWeek> = listOf(DayOfWeek.MONDAY),
    atTime: LocalTime = LocalTime.now(),
    modifiedAt: LocalDateTime = LocalDateTime.now(),
): Plant {
    return Plant(
        id = id.toString(),
        details = PlantDetails(
            name = "Mitchel Cortez",
            size = PlantSize.Medium.name,
            description = "fermentum",
            imageSrcUri = "",
        ),
        wateringInfo = WateringInfo(
            time = atTime,
            days = waterDays,
            amount = "240ml",
            datesWatered = listOf()
        ),
        userLastModifiedDate = modifiedAt
    )
}

fun plantForgotten(date: LocalDateTime = LocalDateTime.now()): Plant {
    return plant(
        waterDays = listOf(date.minusDays(1).dayOfWeek),
        atTime = LocalTime.of(date.hour, date.minute, 0),
        modifiedAt = date.minusDays(2),
    )
}

fun plantNeedsWater(date: LocalDateTime, modifiedAt: LocalDateTime = LocalDateTime.now()): Plant {
    return plant(
        waterDays = listOf(date.dayOfWeek),
        atTime = LocalTime.of(date.hour, date.minute, 0),
        modifiedAt = modifiedAt
    )
}

fun plantNeedsWaterNow(date: LocalDateTime = LocalDateTime.now()): Plant {
    return plant(
        waterDays = listOf(date.dayOfWeek),
        atTime = date.toLocalTime(),
        modifiedAt = date.minusHours(1),
    )
}