package com.sunrisekcdeveloper.pureplanting.workers

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Parcelable
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.sunrisekcdeveloper.home.models.PlantTabFilter
import com.sunrisekcdeveloper.notification.domain.PlantNotificationType
import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import com.sunrisekcdeveloper.pureplanting.MainActivity
import com.sunrisekcdeveloper.pureplanting.R
import kotlinx.parcelize.Parcelize
import com.sunrisekcdeveloper.notification.domain.Notification as DomainNotification

class SystemNotification(
    private val ctx: Context,
    private val plantRepository: PlantRepository,
) {
    private val isNotificationPermissionGranted: Boolean
        get() {
            // Android 13 (API 33) requires user permission
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else true
        }

    @SuppressLint("MissingPermission")
    suspend fun send(domainNotification: DomainNotification) {
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

    private suspend fun createNotification(type: PlantNotificationType): Notification {

        val intent = Intent(ctx, MainActivity::class.java).apply {
            putExtra(MainActivity.DEEPLINK_KEY, deeplinkDestinationFrom(type))
        }

        val pendingIntent = PendingIntent.getActivity(
            ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return Notification.Builder(ctx, type.id)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(type.title)
            .setContentText(type.body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    private suspend fun deeplinkDestinationFrom(type: PlantNotificationType): DeeplinkDestination {
        return if (type.targetPlants.size == 1) {
            val plant = firstPlant(type)
            if (plant != null) {
                DeeplinkDestination.Detail(plant)
            } else {
                DeeplinkDestination.Home()
            }
        } else {
            when (type) {
                is PlantNotificationType.ForgotToWater -> DeeplinkDestination.Home(PlantTabFilter.FORGOT_TO_WATER)
                is PlantNotificationType.NeedsWater -> DeeplinkDestination.Home(PlantTabFilter.UPCOMING)
            }
        }
    }

    private suspend fun firstPlant(notificationType: PlantNotificationType): Plant? {
        return notificationType.targetPlants.firstOrNull()?.run {
            plantRepository.find(this)
        }
    }
}

sealed class DeeplinkDestination : Parcelable {

    @Parcelize
    data class Detail(val plant: Plant) : DeeplinkDestination()

    @Parcelize
    data class Home(val selectedFilter: PlantTabFilter = PlantTabFilter.UPCOMING) : DeeplinkDestination()
}