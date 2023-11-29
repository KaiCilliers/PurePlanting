package com.sunrisekcdeveloper.reminders

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.sunrisekcdeveloper.feature.reminders.R
import com.sunrisekcdeveloper.notification.NotificationDomain
import com.sunrisekcdeveloper.notification.PlantNotificationType

class SystemNotification(
    private val ctx: Context,
) {
    private val isNotificationPermissionGranted: Boolean
        get() {
            // Android 13 (API 33) requires user permission
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else true
        }

    @SuppressLint("MissingPermission")
    fun send(domainNotification: NotificationDomain) {
        ensureChannelExists(domainNotification.type)
        val notification: Notification = createNotification(domainNotification.type)
        if (isNotificationPermissionGranted) {
            NotificationManagerCompat.from(ctx).notify(domainNotification.id.sumOf { if (it.isDigit()) it.digitToInt() else 0 }, notification)
        }
    }

    private fun ensureChannelExists(type: PlantNotificationType) {
        val channel = when (type) {
            is PlantNotificationType.ForgotToWater -> {
                NotificationChannel(
                    type.id,
                    "Forgot to Water Plant Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Shows a notification whenever you've missed the specified date scheduled to water one of your plants"
                }
            }
            is PlantNotificationType.NeedsWater -> {
                NotificationChannel(
                    type.id,
                    "Daily Reminder of Plants to Water Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Shows a notification if you have any plants that need to be watered today"
                }
            }
        }
        (ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?)?.apply {
            createNotificationChannel(channel)
        }
    }

    private fun createNotification(type: PlantNotificationType): Notification {
        return Notification.Builder(ctx, type.id)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(type.notificationTitle)
            .setContentText(type.notificationContent)
            .build()
    }
}