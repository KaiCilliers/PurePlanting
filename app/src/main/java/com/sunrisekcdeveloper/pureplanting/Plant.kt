package com.sunrisekcdeveloper.pureplanting

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import java.util.Stack
import java.util.UUID

data class Plant(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val size: String,
    val description: String,
    val imageSrcUri: String,
    val wateringInfo: WateringInfo,
    val previousWaterDates: Stack<LocalDateTime> = Stack()
) {

    private val nextWateringDate: LocalDateTime? = null

    fun nextWateringDate(now: LocalDateTime): LocalDateTime {
        return nextWateringDate ?: run {
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
            }
        }
    }
}