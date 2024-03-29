package com.sunrisekcdeveloper.pureplanting.domain.notification

import com.sunrisekcdeveloper.pureplanting.library.db_tables.notification.NotificationEntity
import com.sunrisekcdeveloper.pureplanting.library.db_tables.notification.NotificationEntityType

fun Notification.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        createdAt = created,
        seen = seen,
        targetPlants = type.targetPlants,
        title = type.title,
        content = type.body,
        type = when (type) {
            is PlantNotificationType.ForgotToWater -> NotificationEntityType.FORGOT_TO_WATER
            is PlantNotificationType.NeedsWater -> NotificationEntityType.NEEDS_WATER
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