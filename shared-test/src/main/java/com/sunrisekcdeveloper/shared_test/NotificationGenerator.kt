package com.sunrisekcdeveloper.shared_test

import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationDomain
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.PlantNotificationType
import java.time.LocalDateTime
import java.util.UUID

fun unreadNotification(
    type: PlantNotificationType = PlantNotificationType.NeedsWater(targetPlants = listOf("1")),
    created: LocalDateTime = LocalDateTime.now(),
    id: String = UUID.randomUUID().toString(),
): NotificationDomain {
    return NotificationDomain(
        id = id,
        created = created,
        seen = false,
        type = type,
    )
}

fun forgotToWaterNotification(
    created: LocalDateTime = LocalDateTime.now(),
    id: String = UUID.randomUUID().toString(),
): NotificationDomain {
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
): NotificationDomain {
    return unreadNotification(
        type = PlantNotificationType.NeedsWater(
            targetPlants = listOf("1")
        ),
         created = created,
        id = id,
    )
}