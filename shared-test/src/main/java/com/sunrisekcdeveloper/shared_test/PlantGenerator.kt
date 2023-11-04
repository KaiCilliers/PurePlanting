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
    wateringTime: LocalTime = LocalTime.now(),
    previousWateringDate: List<LocalDateTime> = emptyList()
): Plant {
    return Plant(
        id = id,
        details = PlantDetails(
        name = "Mitchel Cortez",
        size = PlantSize.Medium.name,
        description = "fermentum",
        imageSrcUri = "",
        ),
        wateringInfo = WateringInfo(
            atHour = wateringTime.hour,
            days = waterDays,
            amount = "240 ml",
            nextWateringDay = LocalDateTime.now(),
            previousWaterDates = previousWateringDate,
            atMin = wateringTime.minute
        )
    )
}

fun plant(
    name: String = "Test Plant",
    nextWateringDate: LocalDateTime,
    previousWateredDates: List<LocalDateTime> = emptyList(),
): Plant {
    return Plant(
        id = UUID.randomUUID(),
        details = PlantDetails(
            name = name,
            size = "Small",
            description = "Desc",
            imageSrcUri = "imgs src"
        ),
        wateringInfo = WateringInfo(
            atHour = nextWateringDate.hour,
            atMin = nextWateringDate.minute,
            days = listOf(nextWateringDate.dayOfWeek),
            amount = "300 ml",
            nextWateringDay = nextWateringDate,
            previousWaterDates = previousWateredDates
        )
    )
}

fun plantThatNeedsWaterSoon(
    today: LocalDateTime,
    offsetFutureDays: Long = 0,
): Plant {
    return plant(
        waterDays = listOf(today.plusDays(1 + offsetFutureDays).dayOfWeek),
        wateringTime = today.plusHours(1).toLocalTime()
    ).nextWateringDate(today)
}