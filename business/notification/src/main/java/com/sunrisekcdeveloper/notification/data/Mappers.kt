package com.sunrisekcdeveloper.notification.data

import com.sunrisekcdeveloper.notification.Notification
import com.sunrisekcdeveloper.notification.NotificationEntityType
import com.sunrisekcdeveloper.notification.PlantNotificationType

fun Notification.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        createdAt = created,
        seen = seen,
        targetPlants = type.targetPlants,
        title = type.title,
        content = type.body,
        type = when (type) {
            is PlantNotificationType.ForgotToWater -> com.sunrisekcdeveloper.notification.NotificationEntityType.FORGOT_TO_WATER
            is PlantNotificationType.NeedsWater -> com.sunrisekcdeveloper.notification.NotificationEntityType.NEEDS_WATER
        }
    )
}

fun NotificationEntity.toNotification(): Notification {
    return Notification(
        id = id,
        created = createdAt,
        seen = seen,
        type = when(type) {
            NotificationEntityType.FORGOT_TO_WATER -> PlantNotificationType.ForgotToWater(
                targetPlants = targetPlants,
                id = id,
                title = title,
                body = content,
            )
            NotificationEntityType.NEEDS_WATER -> PlantNotificationType.NeedsWater(
                targetPlants = targetPlants,
                id = id,
                title = title,
                body = content,
            )
        }
    )
}