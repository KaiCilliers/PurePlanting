package com.sunrisekcdeveloper.notification

import java.time.LocalDateTime
import java.util.UUID

data class NotificationDomain(
    val id: String = UUID.randomUUID().toString(),
    val created: LocalDateTime,
    val seen: Boolean,
    val type: PlantNotificationType,
) {
    companion object {
        fun createWaterSoon(targetPlantIds: List<String>): NotificationDomain {
            return create(
                PlantNotificationType.NeedsWater(
                    targetPlants = targetPlantIds,
                    notificationContent = "Hey, you have ${targetPlantIds.size} plants to water today"
                )
            )
        }

        fun createForgotToWater(targetPlantIds: List<String>): NotificationDomain {
            return create(
                PlantNotificationType.ForgotToWater(
                    targetPlants = targetPlantIds,
                    notificationContent = "Hey, you forgot to water your"
                )
            )
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