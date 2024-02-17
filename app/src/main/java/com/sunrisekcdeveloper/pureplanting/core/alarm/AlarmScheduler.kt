package com.sunrisekcdeveloper.pureplanting.core.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.ZoneId

interface AlarmScheduler {
    suspend fun schedule(alarmInfo: AlarmInfo)
    suspend fun cancel(alarmInfo: AlarmInfo)

    class Default(
        private val context: Context,
        private val alarmInfoRepo: AlarmInfoRepository
    ) : AlarmScheduler {

        private val alarmManager = context.getSystemService(AlarmManager::class.java)

        override suspend fun schedule(alarmInfo: AlarmInfo) {
            val containsAlarm = alarmInfoRepo.all().any {
                it.time == alarmInfo.time && it.type == alarmInfo.type
            }

            if(containsAlarm) return

            alarmInfoRepo.add(alarmInfo)

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra(KEY_ALARM_TYPE, alarmInfo.type)
            }
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                alarmInfo.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
                alarmInfo.repeatingInterval,
                PendingIntent.getBroadcast(
                    context,
                    alarmInfo.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }

        override suspend fun cancel(alarmInfo: AlarmInfo) {
            alarmInfoRepo.remove(alarmInfo)
            alarmManager.cancel(
                PendingIntent.getBroadcast(
                    context,
                    alarmInfo.hashCode(),
                    Intent(context, Class.forName("com.sunrisekcdeveloper.pureplanting.AlarmReceiver")::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }

    companion object {
        const val KEY_ALARM_TYPE = "EXTRA_ALARM_TYPE"
    }
}

