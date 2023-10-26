package com.sunrisekcdeveloper.pureplanting

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import java.util.Stack
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
            nextWateringDate(
                now,
                wateringInfo.days,
                wateringInfo.atHour
            )
        }.also { nextWateringDate = it }

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

    companion object {
        fun createNewPlant(
            imageSrc: String,
            name: String,
            description: String,
            size: String,
            wateringDays: List<DayOfWeek>,
            wateringHour: Int,
            wateringAmount: String,
        ): Plant {
            return createNewPlantWithId(
                id = UUID.randomUUID(),
                imageSrc = imageSrc,
                name = name,
                description = description,
                size = size,
                wateringDays = wateringDays,
                wateringHour = wateringHour,
                wateringAmount = wateringAmount,
            )
        }

        fun createNewPlantWithId(
            id: UUID,
            imageSrc: String,
            name: String,
            description: String,
            size: String,
            wateringDays: List<DayOfWeek>,
            wateringHour: Int,
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
                    atHour = wateringHour,
                    days = wateringDays,
                    amount = wateringAmount,
                    previousWaterDates = Stack(),
                    nextWateringDay = nextWateringDate(LocalDateTime.now(), wateringDays, wateringHour)
                )
            )
        }

        fun nextWateringDate(
            now: LocalDateTime,
            wateringDays: List<DayOfWeek>,
            wateringHour: Int,
        ): LocalDateTime {
            val wateringDaysSorted = wateringDays.sorted()
            val isTodayWateringDay = wateringDaysSorted.contains(now.dayOfWeek)
            val isWaterHourExceeded = now.hour >= wateringHour
            return when {
                isTodayWateringDay && !isWaterHourExceeded -> {
                    now.with(LocalTime.of(wateringHour, 0))
                }

                else -> {
                    val nextWaterDay = if (wateringDaysSorted.any { it > now.dayOfWeek }) {
                        wateringDaysSorted.first { it > now.dayOfWeek }
                    } else {
                        wateringDaysSorted.first()
                    }
                    now.with(TemporalAdjusters.next(nextWaterDay)).with(LocalTime.of(wateringHour, 0))
                }
            }
        }
    }

}