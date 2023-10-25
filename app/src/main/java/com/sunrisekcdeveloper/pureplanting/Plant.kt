package com.sunrisekcdeveloper.pureplanting

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import java.util.UUID

data class Plant(
    val id: UUID = UUID.randomUUID(),
    val details: PlantDetails,
    val wateringInfo: WateringInfo,
) {

    private var nextWateringDate: LocalDateTime? = null

    val hasBeenWatered: Boolean
        get() = wateringInfo.previousWaterDates.peek() > wateringInfo.nextWateringDay

    fun nextWateringDate(now: LocalDateTime): Plant {
        val date = nextWateringDate ?: run {
            val wateringDaysSorted = wateringInfo.days.sorted()
            val isTodayWateringDay = wateringDaysSorted.contains(now.dayOfWeek)
            val isWaterHourExceeded = now.hour >= wateringInfo.atHour
            when {
                isTodayWateringDay && !isWaterHourExceeded -> {
                    now.with(LocalTime.of(wateringInfo.atHour, 0))
                }

                else -> {
                    val nextWaterDay = if (wateringDaysSorted.any { it > now.dayOfWeek }) {
                        wateringDaysSorted.first { it > now.dayOfWeek }
                    } else {
                        wateringDaysSorted.first()
                    }
                    now.with(TemporalAdjusters.next(nextWaterDay)).with(LocalTime.of(wateringInfo.atHour, 0))
                }
            }.also { nextWateringDate = it }
        }

        return copy(
            wateringInfo = wateringInfo.copy(
                nextWateringDay = date
            )
        )
    }

    fun water(): Plant {
        return copy(
            wateringInfo = wateringInfo.copy(
                previousWaterDates = wateringInfo.previousWaterDates.apply { push(LocalDateTime.now()) }
            )
        )
    }

    fun undoPreviousWatering(): Plant {
        return copy(
            wateringInfo = wateringInfo.copy(
                previousWaterDates = wateringInfo.previousWaterDates.apply { pop() }
            )
        )
    }

    fun needsWaterSoon(now: LocalDateTime): Boolean {
        return wateringInfo.nextWateringDay.dayOfWeek == now.dayOfWeek
                || wateringInfo.nextWateringDay.dayOfWeek == now.dayOfWeek.plus(1)
    }

    fun forgotToWater(now: LocalDateTime): Boolean {
        return wateringInfo.nextWateringDay.dayOfWeek <= now.minusDays(1).dayOfWeek
    }

}