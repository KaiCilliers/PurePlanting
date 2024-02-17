package com.sunrisekcdeveloper.pureplanting.domain.notification

import java.time.LocalDateTime
import java.util.UUID

fun unreadNotification(
    type: PlantNotificationType = PlantNotificationType.NeedsWater(targetPlants = listOf("1")),
    created: LocalDateTime = LocalDateTime.now(),
    id: String = UUID.randomUUID().toString(),
): Notification {
    return Notification(
        id = id,
        created = created,
        seen = false,
        type = type,
    )
}

fun forgotToWaterNotification(
    created: LocalDateTime = LocalDateTime.now(),
    id: String = UUID.randomUUID().toString(),
): Notification {
    return unreadNotification(
        type = PlantNotificationType.ForgotToWater(
            targetPlants = listOf("1")
        ),
        created = created,
        id = id,
    )
}

fun waterSoonNotification(
    created: LocalDateTime = LocalDateTime.now(),
    id: String = UUID.randomUUID().toString(),
): Notification {
    return unreadNotification(
        type = PlantNotificationType.NeedsWater(
            targetPlants = listOf("1")
        ),
         created = created,
        id = id,
    )
}