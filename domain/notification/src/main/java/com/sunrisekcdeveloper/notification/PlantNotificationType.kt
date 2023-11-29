package com.sunrisekcdeveloper.notification

sealed class PlantNotificationType(
    open val targetPlants: List<String>,
    open val id: String,
    open val notificationTitle: String,
    open val notificationContent: String,
) {
    data class ForgotToWater(
        override val targetPlants: List<String>,
        override val id: String = "FORGOT_TO_WATER_CHANNEL",
        override val notificationTitle: String = "Forgot to water notification",
        override val notificationContent: String = "",
    ) : PlantNotificationType(targetPlants, id, notificationTitle, notificationContent)

    data class NeedsWater(
        override val targetPlants: List<String>,
        override val id: String = "DAILY_WATER_REMINDER_CHANNEL",
        override val notificationTitle: String = "Daily plant notification",
        override val notificationContent: String = "",
    ) : PlantNotificationType(targetPlants, id, notificationTitle, notificationContent)
}