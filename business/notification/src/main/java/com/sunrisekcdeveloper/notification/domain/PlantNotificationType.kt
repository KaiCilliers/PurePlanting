package com.sunrisekcdeveloper.notification.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class PlantNotificationType(
    open val targetPlants: List<String>,
    open val id: String,
    open val title: String,
    open val body: String,
): Parcelable {

    @Parcelize
    data class ForgotToWater(
        override val targetPlants: List<String>,
        override val id: String = "FORGOT_TO_WATER_CHANNEL",
        override val title: String = "Forgot to water notification",
        override val body: String = "",
    ) : PlantNotificationType(targetPlants, id, title, body)

    @Parcelize
    data class NeedsWater(
        override val targetPlants: List<String>,
        override val id: String = "DAILY_WATER_REMINDER_CHANNEL",
        override val title: String = "Daily plant notification",
        override val body: String = "",
    ) : PlantNotificationType(targetPlants, id, title, body)
}