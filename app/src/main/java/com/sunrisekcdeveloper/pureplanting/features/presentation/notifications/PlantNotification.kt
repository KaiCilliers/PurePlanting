package com.sunrisekcdeveloper.pureplanting.features.presentation.notifications

import com.sunrisekcdeveloper.pureplanting.features.presentation.plants.Plant
import java.time.LocalDateTime

sealed class PlantNotification(
    open val seen: Boolean,
    open val createdDate: LocalDateTime,
) {
    data class ForgotToWater(
        val plant: Plant,
        override val seen: Boolean,
        override val createdDate: LocalDateTime
    ) : PlantNotification(seen, createdDate)

    data class PlantsToWaterSummary(
        override val seen: Boolean,
        override val createdDate: LocalDateTime
    ) : PlantNotification(seen, createdDate)
}