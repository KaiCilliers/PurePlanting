package com.sunrisekcdeveloper.pureplanting.util

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
import com.sunrisekcdeveloper.pureplanting.R
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationDomain
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.PlantNotificationType

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
        val channel = NotificationChannel(type.channelId, type.channelName, type.importance).apply {
            description = type.channelDesc
        }
        (ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?)?.apply {
            createNotificationChannel(channel)
        }
    }

    private fun createNotification(type: PlantNotificationType): Notification {
        return Notification.Builder(ctx, type.channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(type.notificationTitle)
            .setContentText(type.notificationContent)
            .build()
    }
}