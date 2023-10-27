package com.sunrisekcdeveloper.shared_test

import com.sunrisekcdeveloper.pureplanting.features.component.plants.Plant
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantDetails
import com.sunrisekcdeveloper.pureplanting.features.component.plants.WateringInfo
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.Stack
import java.util.UUID

fun plant(
    id: UUID = UUID.randomUUID(),
    waterDays: List<DayOfWeek> = listOf(DayOfWeek.MONDAY),
    wateringHour: Int = 12,
    previousWateringDate: Stack<LocalDateTime> = Stack()
): Plant {
    return Plant(
        id = id,
        details = PlantDetails(
        name = "Mitchel Cortez",
        size = "pharetra",
        description = "fermentum",
        imageSrcUri = "",
        ),
        wateringInfo = WateringInfo(
            atHour = wateringHour,
            days = waterDays,
            amount = "dictumst",
            nextWateringDay = LocalDateTime.now(),
            previousWaterDates = previousWateringDate,
        )
    )
}

fun plantThatNeedsWaterSoon(
    today: LocalDateTime,
    offsetFutureDays: Long = 0,
): Plant {
    return plant(
        waterDays = listOf(today.plusDays(1 + offsetFutureDays).dayOfWeek),
        wateringHour = today.hour.plus(1)
    ).nextWateringDate(today)
}