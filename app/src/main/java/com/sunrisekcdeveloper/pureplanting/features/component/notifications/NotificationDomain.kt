package com.sunrisekcdeveloper.pureplanting.features.component.notifications

import java.time.LocalDateTime
import java.util.UUID

data class NotificationDomain(
    val id: String = UUID.randomUUID().toString(),
    val created: LocalDateTime,
    val seen: Boolean,
    val type: PlantNotificationType,
)