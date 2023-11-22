package com.sunrisekcdeveloper.pureplanting.features.component.notifications

fun NotificationDomain.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        createdAt = created,
        seen = seen,
        targetPlants = type.targetPlants,
        systemChannelId = type.channelId,
        systemChannelName = type.channelName,
        systemChannelDescription = type.channelDesc,
        title = type.notificationTitle,
        content = type.notificationContent,
        importanceLevel = type.importance,
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
                channelId = systemChannelId,
                channelName = systemChannelName,
                channelDesc = systemChannelDescription,
                notificationTitle = title,
                notificationContent = content,
                importance = importanceLevel,
            )
            NotificationEntityType.NEEDS_WATER -> PlantNotificationType.NeedsWater(
                targetPlants = targetPlants,
                channelId = systemChannelId,
                channelName = systemChannelName,
                channelDesc = systemChannelDescription,
                notificationTitle = title,
                notificationContent = content,
                importance = importanceLevel,
            )
        }
    )
}