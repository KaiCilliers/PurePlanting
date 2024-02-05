package com.sunrisekcdeveloper.pureplanting

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.sunrisekcdeveloper.alarm.AlarmScheduler
import com.sunrisekcdeveloper.alarm.models.AlarmType
import com.sunrisekcdeveloper.pureplanting.workers.ForgotToWaterReminder
import com.sunrisekcdeveloper.pureplanting.workers.WaterPlantReminder

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val alarmType = intent.getParcelableExtra<AlarmType>(AlarmScheduler.KEY_ALARM_TYPE) ?: return
        println("deadpool - Alarm triggered: $alarmType")

        val request = when (alarmType) {
            is AlarmType.ForgotToWater -> OneTimeWorkRequestBuilder<ForgotToWaterReminder>().build() to ForgotToWaterReminder.TAG
            is AlarmType.NeedsWater -> OneTimeWorkRequestBuilder<WaterPlantReminder>().build() to WaterPlantReminder.TAG
        }

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                request.second, // TAG
                ExistingWorkPolicy.REPLACE,
                request.first
            )
    }
}