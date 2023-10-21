package com.sunrisekcdeveloper.pureplanting

import java.time.Clock
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import java.util.UUID

data class Plant(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val size: String,
    val description: String,
    val imageSrcUri: String,
    val wateringInfo: WateringInfo,
) {
    fun upcomingWateringDate(clock: Clock = Clock.systemDefaultZone()): LocalDateTime {
        val now = LocalDateTime.now(clock)
        val wateringDaysSorted = wateringInfo.days.sorted()

        val isTodayWateringDay = wateringDaysSorted.contains(now.dayOfWeek)
        val isWaterHourExceeded = now.hour >= wateringInfo.atHour

        val next = when {
            isTodayWateringDay && !isWaterHourExceeded -> {
                now.with(LocalTime.of(wateringInfo.atHour, 0))
            }

            else -> {
                val nextWaterDay = if (wateringDaysSorted.any { it > now.dayOfWeek }) {
                    wateringDaysSorted.first { it > now.dayOfWeek }
                } else {
                    wateringDaysSorted.first()
                }
                now
                    .with(TemporalAdjusters.next(nextWaterDay))
                    .with(LocalTime.of(wateringInfo.atHour, 0))
            }
        }
        return next
    }
}