package com.sunrisekcdeveloper.notificationList

import com.sunrisekcdeveloper.notification.PlantNotificationType
import com.zhuinden.flowcombinetuplekt.combineTuple
import com.zhuinden.simplestack.Bundleable
import com.zhuinden.statebundle.StateBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

typealias NotificationsGroupedByDay = Map<Pair<Int, Int>, List<com.sunrisekcdeveloper.notification.NotificationDomain>>

class NotificationListViewModel(
    private val notificationCache: com.sunrisekcdeveloper.notification.NotificationCache
): Bundleable {

    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

    private val _activeFilter = MutableStateFlow(NotificationFilter.ALL)
    val activeFilter: StateFlow<NotificationFilter> = _activeFilter

    val notifications: StateFlow<List<com.sunrisekcdeveloper.notification.NotificationDomain>> = combine(notificationCache.observe(), _activeFilter) { notifications, filter ->
        when (filter) {
            NotificationFilter.ALL -> notifications
            NotificationFilter.FORGOT_TO_WATER -> notifications
                .filter { it.type is PlantNotificationType.ForgotToWater }

            NotificationFilter.NEEDS_WATER -> notifications
                .filter { it.type is PlantNotificationType.NeedsWater }
        }
            .sortedByDescending { it.created }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyList())

    val notificationsGroupedByDay: StateFlow<NotificationsGroupedByDay> = combineTuple(notifications).map { (ungroupedNotifications) ->
        ungroupedNotifications
            .groupBy { it.created.dayOfYear to it.created.year }
            .onEach { println(it) }
            .toSortedMap(
                compareByDescending<Pair<Int, Int>> { (_, year) -> year }
                    .thenByDescending { (day, _) -> day }
            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyMap())

    fun setFilter(type: NotificationFilter) = viewModelScope.launch {
        notifications.value.forEach {
            notificationCache.markAsSeen(it.id)
        }
        _activeFilter.update { type }
    }

    override fun toBundle(): StateBundle = StateBundle().apply {
        putString("activeNotificationFilter", activeFilter.value.toString())
    }

    override fun fromBundle(bundle: StateBundle?) {
        bundle?.run {
            _activeFilter.update { NotificationFilter.valueOf(getString("activeNotificationFilter", NotificationFilter.ALL.toString())) }
        }
    }
}