package com.sunrisekcdeveloper.pureplanting.core.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.sunrisekcdeveloper.pureplanting.domain.plant.PlantRepository
import java.time.LocalTime
import java.time.ZoneId

interface AlarmScheduler {
    suspend fun schedule(alarmInfo: AlarmInfo)
    suspend fun cancel(alarmInfo: AlarmInfo)

    class Default(
        private val context: Context,
        private val alarmInfoRepo: AlarmInfoRepository,
        private val plantRepository: PlantRepository,
    ) : AlarmScheduler {

        private val alarmManager = context.getSystemService(AlarmManager::class.java)

        override suspend fun schedule(alarmInfo: AlarmInfo) {
            val containsAlarm = alarmInfoRepo.all().any {
                it.time == alarmInfo.time && it.type == alarmInfo.type
            }

            if (containsAlarm) return

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

            val allAlarms = alarmInfoRepo.all()
            val allPlantWateringTimes = allPlantWateringTimes().toHashSet()

            allAlarms
                .filterNot { alarm ->
                    allPlantWateringTimes.contains(alarm.time.toLocalTime()) || alarm.type == AlarmType.ForgotToWater
                }
                .forEach { alarm -> cancel(alarm) }
        }

        private suspend fun allPlantWateringTimes(): List<LocalTime> {
            return plantRepository.all().map { it.wateringInfo.time }
        }

        override suspend fun cancel(alarmInfo: AlarmInfo) {
            alarmInfoRepo.remove(alarmInfo)
            alarmManager.cancel(
                PendingIntent.getBroadcast(
                    context,
                    alarmInfo.hashCode(),
                    Intent(context, AlarmReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }

    companion object {
        const val KEY_ALARM_TYPE = "EXTRA_ALARM_TYPE"
    }
}

