package com.sunrisekcdeveloper.notification

import java.time.LocalDateTime
import java.util.UUID

data class Notification(
    val id: String = UUID.randomUUID().toString(),
    val created: LocalDateTime = LocalDateTime.now(),
    val seen: Boolean,
    val type: PlantNotificationType,
) {
    companion object {
        fun createWaterSoon(targetPlants: List<PlantTag>): Notification {
            return create(
                PlantNotificationType.NeedsWater(
                    targetPlants = targetPlants.map { it.id },
                    body = if (targetPlants.size == 1) {
                        "Hey, \"${targetPlants.first().name}\" needs water soon"
                    } else {
                        "Hey, you have ${targetPlants.size} plants that need water soon"
                    }
                )
            )
        }

        // todo replace with PlantTag to have access to plant name
        fun createForgotToWater(targetPlants: List<PlantTag>): Notification {
            return create(
                PlantNotificationType.ForgotToWater(
                    targetPlants = targetPlants.map { it.id },
                    body = if (targetPlants.size == 1) {
                        "Hey, you forgot to water \"${targetPlants.first().name}\" yesterday"
                    } else {
                        "Hey, you forgot to water some plants yesterday"
                    }
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