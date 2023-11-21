package com.sunrisekcdeveloper.pureplanting.features.component.notifications

import com.sunrisekcdeveloper.pureplanting.features.component.plants.Plant
import java.time.LocalDateTime
import java.util.UUID

data class NotificationDomain(
    val id: String = UUID.randomUUID().toString(),
    val created: LocalDateTime,
    val seen: Boolean,
    val type: PlantNotificationType,
) {
    companion object {
        fun createWaterSoon(targetPlants: List<Plant>): NotificationDomain {
            return create(PlantNotificationType.NeedsWater(
                targetPlants = targetPlants.map { it.id },
                notificationContent = "Hey, you have ${targetPlants.size} plants to water today"
            ))
        }

        fun createForgotToWater(targetPlants: List<Plant>): NotificationDomain {
            return create(PlantNotificationType.ForgotToWater(
                targetPlants = targetPlants.map { it.id },
                notificationContent = "Hey, you forgot to water your"
            ))
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