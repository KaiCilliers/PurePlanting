package com.sunrisekcdeveloper.pureplanting.features.presentation.notifications

sealed class NotificationFilter {
    data object All : NotificationFilter()
    data object NeedsWater : NotificationFilter()
    data object ForgotToWater : NotificationFilter()
}