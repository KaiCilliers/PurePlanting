package com.sunrisekcdeveloper.pureplanting.app.workers

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
import com.sunrisekcdeveloper.pureplanting.business.notification.PlantNotificationType
import com.sunrisekcdeveloper.pureplanting.business.plant.Plant
import com.sunrisekcdeveloper.pureplanting.business.plant.PlantRepository
import com.sunrisekcdeveloper.pureplanting.app.MainActivity
import com.sunrisekcdeveloper.pureplanting.R
import com.sunrisekcdeveloper.pureplanting.components.home.models.PlantTabFilter
import kotlinx.parcelize.Parcelize
import com.sunrisekcdeveloper.pureplanting.business.notification.Notification as DomainNotification

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
        val notification: Notification = createNotification(domainNotification)
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

    private suspend fun createNotification(domain: DomainNotification): Notification {

        val intent = Intent(ctx, MainActivity::class.java).apply {
            putExtra(MainActivity.DEEPLINK_KEY, deeplinkDestinationFrom(domain))
        }

        val pendingIntent = PendingIntent.getActivity(
            ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return Notification.Builder(ctx, domain.type.id)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(domain.type.title)
            .setContentText(domain.type.body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    private suspend fun deeplinkDestinationFrom(domain: DomainNotification): DeeplinkDestination {
        return if (domain.type.targetPlants.size == 1) {
            val plant = firstPlant(domain.type)
            if (plant != null) {
                DeeplinkDestination.Detail(domain.id, plant)
            } else {
                DeeplinkDestination.Home(domain.id)
            }
        } else {
            when (domain.type) {
                is PlantNotificationType.ForgotToWater -> DeeplinkDestination.Home(domain.id, PlantTabFilter.FORGOT_TO_WATER)
                is PlantNotificationType.NeedsWater -> DeeplinkDestination.Home(domain.id, PlantTabFilter.UPCOMING)
            }
        }
    }

    private suspend fun firstPlant(notificationType: PlantNotificationType): Plant? {
        return notificationType.targetPlants.firstOrNull()?.run {
            plantRepository.find(this)
        }
    }
}

sealed class DeeplinkDestination(
    open val notificationId: String,
) : Parcelable {

    @Parcelize
    data class Detail(
        override val notificationId: String,
        val plant: Plant
    ) : DeeplinkDestination(notificationId)

    @Parcelize
    data class Home(
        override val notificationId: String,
        val selectedFilter: PlantTabFilter = PlantTabFilter.UPCOMING
    ) : DeeplinkDestination(notificationId)
}