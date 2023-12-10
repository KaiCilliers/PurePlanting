package com.sunrisekcdeveloper.plantList

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface NotificationIconComponent {

    fun interface Router {
        fun goToNotificationList()
    }

    val isNotificationBadgeVisible: StateFlow<Boolean>

    fun onIconClick()

    class Fake : NotificationIconComponent {
        override val isNotificationBadgeVisible = MutableStateFlow(true)

        override fun onIconClick() = Unit
    }

}