package com.sunrisekcdeveloper.notification

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sunrisekcdeveloper.notification.NotificationEntityType
import java.time.LocalDateTime

@Entity
data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val createdAt: LocalDateTime,
    val seen: Boolean,
    val targetPlants: List<String>,
    val title: String,
    val content: String,
    val type: NotificationEntityType
)