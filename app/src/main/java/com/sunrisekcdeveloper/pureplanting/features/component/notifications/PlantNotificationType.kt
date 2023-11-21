package com.sunrisekcdeveloper.pureplanting.features.component.notifications

import android.app.NotificationManager

sealed class PlantNotificationType(
    open val targetPlants: List<String>,
    open val channelId: String,
    open val channelName: String,
    open val channelDesc: String,
    open val notificationTitle: String,
    open val notificationContent: String,
    open val importance: Int,
) {
    data class ForgotToWater(
        override val targetPlants: List<String>,
        override val channelId: String = "FORGOT_TO_WATER_CHANNEL",
        override val channelName: String = "Forgot to Water Plant Notifications",
        override val channelDesc: String = "Shows a notification whenever you've missed the specified date scheduled to water one of your plants",
        override val notificationTitle: String = "Forgot to water notification",
        override val notificationContent: String = "",
        override val importance: Int = NotificationManager.IMPORTANCE_HIGH,
    ) : PlantNotificationType(targetPlants, channelId, channelName, channelDesc, notificationTitle, notificationContent, importance)

    data class NeedsWater(
        override val targetPlants: List<String>,
        override val channelId: String = "DAILY_WATER_REMINDER_CHANNEL",
        override val channelName: String = "Daily Reminder of Plants to Water Notifications",
        override val channelDesc: String = "Shows a notification if you have any plants that need to be watered today",
        override val notificationTitle: String = "Daily plant notification",
        override val notificationContent: String = "",
        override val importance: Int = NotificationManager.IMPORTANCE_HIGH,
    ) : PlantNotificationType(targetPlants, channelId, channelName, channelDesc, notificationTitle, notificationContent, importance)
}