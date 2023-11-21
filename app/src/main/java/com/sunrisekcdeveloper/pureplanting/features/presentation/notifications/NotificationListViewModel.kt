package com.sunrisekcdeveloper.pureplanting.features.presentation.notifications

import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationDomain
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationsCache
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.PlantNotificationType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationListViewModel(
    private val notificationsCache: NotificationsCache
) {

    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

    private val _activeFilter = MutableStateFlow<NotificationFilter>(NotificationFilter.All)
    val activeFilter: StateFlow<NotificationFilter> = _activeFilter

    val notifications: StateFlow<List<NotificationDomain>> = combine(notificationsCache.observe(), _activeFilter) { notifications, filter ->
        when(filter) {
            NotificationFilter.All -> notifications
            NotificationFilter.ForgotToWater -> notifications
                .filter { it.type is PlantNotificationType.ForgotToWater }
            NotificationFilter.NeedsWater -> notifications
                .filter { it.type is PlantNotificationType.NeedsWater }
        }
            .sortedByDescending { it.created }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyList())

    fun setFilter(type: NotificationFilter) = viewModelScope.launch {
        notifications.value.forEach {
            notificationsCache.markAsSeen(it.id)
        }
        _activeFilter.update { type }
    }
}