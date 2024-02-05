package com.sunrisekcdeveloper.alarm.models

import com.sunrisekcdeveloper.db_tables.alarm.AlarmInfoEntity
import com.sunrisekcdeveloper.alarm.models.AlarmType.Companion.toAlarmType
import java.time.LocalDateTime

data class AlarmInfo(
    val time: LocalDateTime,
    val type: AlarmType,
    val repeatingInterval: Long = 1 * 60 * 1000
) {
    fun toEntity(): AlarmInfoEntity {
        return AlarmInfoEntity(
            scheduledTime = this.time,
            createdAt = LocalDateTime.now(),
            repeatingInterval = this.repeatingInterval,
            type = this.type.name
        )
    }

    companion object {
        fun AlarmInfoEntity.toDomain(): AlarmInfo {
            return AlarmInfo(
                time = this.scheduledTime,
                type = this.type.toAlarmType()!!,
                repeatingInterval = this.repeatingInterval
            )
        }
    }
}