package com.sunrisekcdeveloper.plantList

import com.sunrisekcdeveloper.notification.domain.NotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

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

    class Default(
        notificationRepository: NotificationRepository,
        private val router: Router
    ) : NotificationIconComponent {

        private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

        override val isNotificationBadgeVisible = notificationRepository
            .observe()
            .map { allNotifications -> allNotifications.any { !it.seen } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), false)

        override fun onIconClick() {
            router.goToNotificationList()
        }
    }

}