package com.sunrisekcdeveloper.notificationList

import com.sunrisekcdeveloper.notification.domain.Notification
import com.sunrisekcdeveloper.notification.domain.NotificationRepository
import com.sunrisekcdeveloper.notification.domain.PlantNotificationType
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import com.zhuinden.simplestack.Bundleable
import com.zhuinden.statebundle.StateBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// perhpas split the diagrams a bit in plantUML - create separate SVG for domain plant and notifications
typealias NotificationGroupedByDay = Map<Pair<Int, Int>, List<Notification>>

class DefaultNotificationListComponent(
    notificationRepository: NotificationRepository,
    private val plantRepository: PlantRepository,
    private val router: NotificationListComponent.Router
) : NotificationListComponent, Bundleable {

    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

    override val filter = MutableStateFlow(NotificationFilter.ALL)

    override val notifications: StateFlow<NotificationGroupedByDay> =
        combine(notificationRepository.observe(), filter) { notifications, activeFilter ->
            when (activeFilter) {
                NotificationFilter.ALL -> notifications
                NotificationFilter.FORGOT_TO_WATER -> notifications
                    .filter { it.type is PlantNotificationType.ForgotToWater }

                NotificationFilter.NEEDS_WATER -> notifications
                    .filter { it.type is PlantNotificationType.NeedsWater }
            }
                .sortedByDescending { it.created }
                .groupBy { it.created.dayOfYear to it.created.year }
                .onEach { println(it) }
                .toSortedMap(
                    compareByDescending<Pair<Int, Int>> { (_, year) -> year }
                        .thenByDescending { (day, _) -> day }
                )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyMap())

    override fun onFilterChanged(filter: NotificationFilter) {
        this.filter.value = filter
    }

    override fun onNotificationClick(notification: Notification) {
        if (notification.type.targetPlants.size == 1) {
            viewModelScope.launch {
                val plant = plantRepository.find(notification.type.targetPlants.first())
                if (plant != null) {
                    router.goToDetail(plant)
                }
            }
        } else {
            router.goToMain(notification.type)
        }
    }

    override fun onBackClick() {
        router.goBack()
    }

    override fun toBundle(): StateBundle = StateBundle().apply {
        putString("activeNotificationFilter", filter.value.toString())
    }

    override fun fromBundle(bundle: StateBundle?) {
        bundle?.run {
            filter.value = NotificationFilter.valueOf(getString("activeNotificationFilter", NotificationFilter.ALL.toString()))
        }
    }
}