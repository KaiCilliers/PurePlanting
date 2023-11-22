package com.sunrisekcdeveloper.pureplanting.features.component.notifications

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val createdAt: LocalDateTime,
    val seen: Boolean,
    val targetPlants: List<String>,
    val systemChannelId: String,
    val systemChannelName: String,
    val systemChannelDescription: String,
    val title: String,
    val content: String,
    val importanceLevel: Int,
    val type: NotificationEntityType
)