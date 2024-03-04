package com.sunrisekcdeveloper.pureplanting.core.database.alarm

import androidx.room.Entity
import java.time.LocalDateTime

@Entity(primaryKeys = ["scheduledTime", "type"])
data class AlarmInfoEntity(
    val scheduledTime: LocalDateTime,
    val createdAt: LocalDateTime,
    val repeatingInterval: Long,
    val type: String,
)