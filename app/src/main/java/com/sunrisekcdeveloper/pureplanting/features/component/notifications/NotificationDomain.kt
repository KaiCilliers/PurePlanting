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
        fun createWaterSoon(plantsToWaterCount: Int): NotificationDomain {
            return create(PlantNotificationType.DailyWaterReminder(
                notificationContent = "Hey, you have $plantsToWaterCount plants to water today"
            ))
        }

        fun createForgotToWater(targetPlant: Plant): NotificationDomain {
            return create(PlantNotificationType.ForgotToWater(
                targetPlant = targetPlant,
                notificationContent = "Hey, you forgot to water your ${targetPlant.details.name}"
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