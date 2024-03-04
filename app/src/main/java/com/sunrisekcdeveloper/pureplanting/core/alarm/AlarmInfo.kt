package com.sunrisekcdeveloper.pureplanting.core.alarm

import com.sunrisekcdeveloper.pureplanting.core.alarm.AlarmType.Companion.toAlarmType
import com.sunrisekcdeveloper.pureplanting.core.database.alarm.AlarmInfoEntity
import java.time.LocalDateTime

data class AlarmInfo(
    val time: LocalDateTime,
    val type: AlarmType,
) {
    fun toEntity(): AlarmInfoEntity {
        return AlarmInfoEntity(
            scheduledTime = this.time,
            createdAt = LocalDateTime.now(),
            type = this.type.name
        )
    }

    companion object {
        fun AlarmInfoEntity.toDomain(): AlarmInfo {
            return AlarmInfo(
                time = this.scheduledTime,
                type = this.type.toAlarmType()!!,
            )
        }
    }
}