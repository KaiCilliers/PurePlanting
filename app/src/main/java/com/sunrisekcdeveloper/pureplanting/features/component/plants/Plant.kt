package com.sunrisekcdeveloper.pureplanting.features.component.plants

import android.os.Parcelable
import com.sunrisekcdeveloper.pureplanting.util.getDayOfWeeksBetween
import com.sunrisekcdeveloper.pureplanting.util.getDaysBetween
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
    val userLastModifiedDate: LocalDateTime = LocalDateTime.now()
) : Parcelable {

    val dateLastWatered: LocalDateTime?
        get() = wateringInfo.datesWatered.maxOrNull()

    fun isWatered(today: LocalDateTime): Boolean {
        val dateShouldHaveReceivedWater = previousWaterDate(today)
        return dateLastWatered?.isBefore(dateShouldHaveReceivedWater) == false
    }

    fun needsWater(today: LocalDateTime): Boolean {
        val dateShouldHaveReceivedWater = previousWaterDate(today)
        val needsWaterToday = dateShouldHaveReceivedWater.getDaysBetween(today) == 0L
        val wateredRecently = dateLastWatered?.getDaysBetween(today) == 0L
        val modifiedTimeIsAfterWateringTime = userLastModifiedDate.toLocalTime().isAfter(wateringInfo.time)
        return needsWaterToday && !wateredRecently && !modifiedTimeIsAfterWateringTime
    }

    fun forgotToWater(today: LocalDateTime): Boolean {
        val daysBetween = userLastModifiedDate.getDaysBetween(today)
        val latestWaterDate = dateLastWatered

        return !needsWater(today) && if (daysBetween >= 7) {
            if (latestWaterDate == null) {
                true
            } else {
                val dateShouldHaveReceivedWater = previousWaterDate(today)
                latestWaterDate.toLocalDate().isBefore(dateShouldHaveReceivedWater.toLocalDate())
            }
        } else {
            val validWeekdays = userLastModifiedDate.getDayOfWeeksBetween(today)
            val dateShouldHaveReceivedWater = previousWaterDate(today)

            if(validWeekdays.contains(dateShouldHaveReceivedWater.dayOfWeek)) {
                if (latestWaterDate == null) {
                    true
                } else {
                    latestWaterDate.toLocalDate().isBefore(dateShouldHaveReceivedWater.toLocalDate())
                }
            } else false
        }
    }

    private fun previousWaterDate(today: LocalDateTime): LocalDateTime {
        return if (wateringInfo.days.contains(today.dayOfWeek)) {
            today
        } else if (wateringInfo.days.contains(today.minusDays(1).dayOfWeek)) {
            today.minusDays(1)
        } else if (wateringInfo.days.contains(today.minusDays(2).dayOfWeek)) {
            today.minusDays(2)
        } else if (wateringInfo.days.contains(today.minusDays(3).dayOfWeek)) {
            today.minusDays(3)
        } else if (wateringInfo.days.contains(today.minusDays(4).dayOfWeek)) {
            today.minusDays(4)
        } else if (wateringInfo.days.contains(today.minusDays(5).dayOfWeek)) {
            today.minusDays(5)
        } else if (wateringInfo.days.contains(today.minusDays(6).dayOfWeek)) {
            today.minusDays(6)
        } else {
            today
        }
    }

    fun water(clock: Clock = Clock.systemDefaultZone()): Plant {
        val history = wateringInfo.datesWatered.toMutableList()
        history.add(LocalDateTime.now(clock))
        return copy(
            wateringInfo = wateringInfo.copy(datesWatered = history)
        )
    }

    fun undoLastWatering(): Plant {
        val history = wateringInfo.datesWatered.toMutableList()
        history.removeLast()
        return copy(wateringInfo = wateringInfo.copy(
            datesWatered = history.toList()
        ))
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
                    datesWatered = emptyList()
                )
            )
        }
    }
}