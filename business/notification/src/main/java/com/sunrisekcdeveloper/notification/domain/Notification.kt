package com.sunrisekcdeveloper.notification.domain

import java.time.LocalDateTime
import java.util.UUID

data class Notification(
    val id: String = UUID.randomUUID().toString(),
    val created: LocalDateTime = LocalDateTime.now(),
    val seen: Boolean,
    val type: PlantNotificationType,
) {
    companion object {
        fun createWaterSoon(targetPlantIds: List<String>): Notification {
            return create(
                PlantNotificationType.NeedsWater(
                    targetPlants = targetPlantIds,
                    body = "Hey, you have ${targetPlantIds.size} plants to water today"
                )
            )
        }

        // todo replace with PlantTag to have access to plant name
        fun createForgotToWater(targetPlantIds: List<String>): Notification {
            return create(
                PlantNotificationType.ForgotToWater(
                    targetPlants = targetPlantIds,
                    body = "Hey, you forgot to water your"
                )
            )
        }

        private fun create(type: PlantNotificationType): Notification {
            return Notification(
                created = LocalDateTime.now(),
                seen = false,
                type = type,
            )
        }
    }
}

data class PlantTag(
    val id: String,
    val name: String,
)