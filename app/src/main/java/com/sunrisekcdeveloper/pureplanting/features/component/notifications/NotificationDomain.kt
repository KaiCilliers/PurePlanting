package com.sunrisekcdeveloper.pureplanting.features.component.notifications

import java.time.LocalDateTime
import java.util.UUID

data class NotificationDomain(
    val id: String = UUID.randomUUID().toString(),
    val created: LocalDateTime,
    val seen: Boolean,
    val type: PlantNotificationType,
) {
    companion object {
        fun createWaterSoon(): NotificationDomain {
            return create(PlantNotificationType.WATER_SOON)
        }

        fun createForgotToWater(): NotificationDomain {
            return create(PlantNotificationType.FORGOT_TO_WATER)
        }

        private fun create(type: PlantNotificationType): NotificationDomain {
            return NotificationDomain(
                created = LocalDateTime.now(),
                seen = false,
                type = type,
            )
        }
    }
}