package com.sunrisekcdeveloper.notification

import com.sunrisekcdeveloper.notification.NotificationDomain
import com.sunrisekcdeveloper.notification.NotificationEntity
import com.sunrisekcdeveloper.notification.NotificationEntityType
import com.sunrisekcdeveloper.notification.PlantNotificationType

fun NotificationDomain.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        createdAt = created,
        seen = seen,
        targetPlants = type.targetPlants,
        title = type.notificationTitle,
        content = type.notificationContent,
        type = when (type) {
            is PlantNotificationType.ForgotToWater -> NotificationEntityType.FORGOT_TO_WATER
            is PlantNotificationType.NeedsWater -> NotificationEntityType.NEEDS_WATER
        }
    )
}

fun NotificationEntity.toNotification(): NotificationDomain {
    return NotificationDomain(
        id = id,
        created = createdAt,
        seen = seen,
        type = when(type) {
            NotificationEntityType.FORGOT_TO_WATER -> PlantNotificationType.ForgotToWater(
                targetPlants = targetPlants,
                id = id,
                notificationTitle = title,
                notificationContent = content,
            )
            NotificationEntityType.NEEDS_WATER -> PlantNotificationType.NeedsWater(
                targetPlants = targetPlants,
                id = id,
                notificationTitle = title,
                notificationContent = content,
            )
        }
    )
}