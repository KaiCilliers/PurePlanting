package com.sunrisekcdeveloper.pureplanting.features.notificationList

import com.sunrisekcdeveloper.pureplanting.features.notificationList.models.NotificationFilter
import com.sunrisekcdeveloper.pureplanting.domain.notification.Notification
import com.sunrisekcdeveloper.pureplanting.domain.notification.NotificationRepository
import com.sunrisekcdeveloper.pureplanting.domain.notification.PlantNotificationType
import com.sunrisekcdeveloper.pureplanting.domain.notification.PlantTag
import com.sunrisekcdeveloper.pureplanting.domain.plant.Plant
import com.sunrisekcdeveloper.pureplanting.domain.plant.PlantRepository
import com.zhuinden.simplestack.Bundleable
import com.zhuinden.simplestack.ScopedServices
import com.zhuinden.statebundle.StateBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

interface NotificationListViewModel {

    val filter: StateFlow<NotificationFilter>

    val notifications: StateFlow<NotificationGroupedByDay>

    val isLoading: StateFlow<Boolean>

    fun onFilterChanged(filter: NotificationFilter)

    fun onNotificationClick(notification: Notification)

    fun onSeenNotifications()

    fun onBackClick()

    interface Router {
        fun goBack()
        fun goToMain(notificationType: PlantNotificationType)
        fun goToDetail(plant: Plant)
    }

    class Fake : NotificationListViewModel {
        override val filter: StateFlow<NotificationFilter> = MutableStateFlow(NotificationFilter.ALL)
        override val isLoading: StateFlow<Boolean> = MutableStateFlow(false)
        override val notifications: StateFlow<NotificationGroupedByDay> = MutableStateFlow(
            mapOf(
                (10 to 2023) to listOf(
                    Notification.createWaterSoon(listOf(
                    PlantTag("1", "plant"),
                    PlantTag("2", "plant"),
                    PlantTag("3", "plant"),
                ))),
                (10 to 2023) to listOf(
                    Notification.createWaterSoon(listOf(
                    PlantTag("4", "plant")
                ))),
                (11 to 2023) to listOf(
                    Notification.createForgotToWater(listOf(
                    PlantTag("1", "plant"),
                    PlantTag("2", "plant"),
                    PlantTag("3", "plant"),
                ))),
                (12 to 2023) to listOf(
                    Notification.createWaterSoon(listOf(
                    PlantTag("1", "plant"),
                    PlantTag("2", "plant")
                ))),
            ).toSortedMap(
                compareByDescending<Pair<Int, Int>> { (_, year) -> year }
                    .thenByDescending { (day, _) -> day }
            )
        )

        override fun onFilterChanged(filter: NotificationFilter) = Unit
        override fun onNotificationClick(notification: Notification) = Unit
        override fun onBackClick() = Unit
        override fun onSeenNotifications() = Unit

    }

    class Default(
        private val notificationRepository: NotificationRepository,
        private val plantRepository: PlantRepository,
        private val router: Router
    ) : NotificationListViewModel, Bundleable, ScopedServices.Activated {

        private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

        override val filter = MutableStateFlow(NotificationFilter.ALL)

        override val isLoading = MutableStateFlow(true)

        override val notifications: StateFlow<NotificationGroupedByDay> =
            combine(notificationRepository.observe(), filter) { notifications, activeFilter ->
                isLoading.value = false
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

        override fun onSeenNotifications() {
            viewModelScope.launch {
                notificationRepository.all().forEach {
                    if (!it.seen) notificationRepository.markAsSeen(it.id)
                }
            }
        }

        override fun onServiceInactive() {
            onSeenNotifications()
        }

        override fun onServiceActive() { }

        override fun toBundle(): StateBundle = StateBundle().apply {
            putString("activeNotificationFilter", filter.value.toString())
        }

        override fun fromBundle(bundle: StateBundle?) {
            bundle?.run {
                filter.value = NotificationFilter.valueOf(getString("activeNotificationFilter", NotificationFilter.ALL.toString()))
            }
        }
    }

}