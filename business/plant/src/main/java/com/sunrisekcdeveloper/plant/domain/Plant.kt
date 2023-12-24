package com.sunrisekcdeveloper.plant.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

@Parcelize
data class Plant(
    val id: String = UUID.randomUUID().toString(),
    val details: PlantDetails,
    val wateringInfo: WateringInfo,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) : Parcelable {

    val dateLastWatered: LocalDateTime?
        get() = wateringInfo.history.lastOrNull()

    fun waterTimeIsUpcoming(today: LocalDateTime): Boolean {
        // 1. todays day of week matces one of the plants watering days
        val needsWater = wateringInfo.days.contains(today.dayOfWeek)
        // 2. plant has not bee watered today
        val hasBeenWateredToday = dateLastWatered?.dayOfYear == today.dayOfYear && dateLastWatered?.year == today.year
       return needsWater && !hasBeenWateredToday
    }

    fun needsWater(today: LocalDateTime): Boolean {
        return missedLatestWateringDate(today) || waterTimeIsUpcoming(today)
    }

    fun missedLatestWateringDate(today: LocalDateTime): Boolean {
        // 1. calculate the latest date the plant needed water
        val dateNeededWater = currentWateringDate(today)
        // 2. that date needs to be after the date watering data was last modified
        val isAfterCreatedDate = dateNeededWater.isAfter(this.wateringInfo.lastModifiedWateringDays)
        // 3. that date needs to be before today
        val isBeforeToday = dateNeededWater.isBefore(today)
        // 4. that date needs to be after the last watering date in history
        val isAfterLatestWatering = if (dateLastWatered != null) dateNeededWater.isAfter(dateLastWatered) else true
        return isAfterCreatedDate && isBeforeToday && isAfterLatestWatering
    }

    fun currentWateringDate(today: LocalDateTime): LocalDateTime {
        var previousDay = today

        while(!wateringInfo.days.contains(previousDay.dayOfWeek)) {
            previousDay = previousDay.minusDays(1)
        }

        return previousDay
    }

    fun water(clock: Clock = Clock.systemDefaultZone()): Plant {
        val history = wateringInfo.history.toMutableList()
        history.add(LocalDateTime.now(clock))
        return copy(
            wateringInfo = wateringInfo.copy(history = history)
        )
    }

    fun undoWatering(): Plant {
        val history = wateringInfo.history.toMutableList()
        history.removeLast()
        return copy(
            wateringInfo = wateringInfo.copy(
                history = history.toList()
            )
        )
    }

    companion object {
        fun createNewPlant(
            imageSrc: String,
            name: String,
            description: String,
            size: String,
            wateringDays: List<DayOfWeek>,
            wateringTime: LocalTime,
            wateringAmount: String,
            createdAt: LocalDateTime = LocalDateTime.now(),
        ): Plant {
            return createNewPlantWithId(
                id = UUID.randomUUID().toString(),
                imageSrc = imageSrc,
                name = name,
                description = description,
                size = size,
                wateringDays = wateringDays,
                wateringTime = wateringTime,
                wateringAmount = wateringAmount,
                createdAt = createdAt,
            )
        }

        fun createNewPlantWithId(
            id: String,
            imageSrc: String,
            name: String,
            description: String,
            size: String,
            wateringDays: List<DayOfWeek>,
            wateringTime: LocalTime,
            wateringAmount: String,
            createdAt: LocalDateTime = LocalDateTime.now(),
        ): Plant {
            return Plant(
                id = id,
                details = PlantDetails(
                    name = name,
                    size = size,
                    description = description,
                    imageSrcUri = imageSrc,
                ),
                wateringInfo = WateringInfo(
                    time = wateringTime,
                    days = wateringDays,
                    amount = wateringAmount,
                    history = emptyList(),
                    lastModifiedWateringDays = createdAt
                )
            )
        }
    }
}