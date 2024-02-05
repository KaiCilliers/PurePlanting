package com.sunrisekcdeveloper.db_tables.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class AlarmInfoEntity(
    @PrimaryKey
    val scheduledTime: LocalDateTime,
    val createdAt: LocalDateTime,
    val repeatingInterval: Long,
    val type: String,
)