package com.sunrisekcdeveloper.notificationList

import com.sunrisekcdeveloper.notification.domain.Notification
import com.sunrisekcdeveloper.notification.domain.PlantNotificationType
import com.sunrisekcdeveloper.plant.domain.Plant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface NotificationListComponent {

    val filter: StateFlow<NotificationFilter>

    val notifications: StateFlow<NotificationGroupedByDay>

    fun onFilterChanged(filter: NotificationFilter)

    fun onNotificationClick(notification: Notification)

    fun onBackClick()

    interface Router {
        fun goBack()
        fun goToMain(notificationType: PlantNotificationType)
        fun goToDetail(plant: Plant)
    }

    class Fake : NotificationListComponent {
        override val filter: StateFlow<NotificationFilter> = MutableStateFlow(NotificationFilter.ALL)
        override val notifications: StateFlow<NotificationGroupedByDay> = MutableStateFlow(
            mapOf(
                (10 to 2023) to listOf(Notification.createWaterSoon(listOf("1,2,3"))),
                (10 to 2023) to listOf(Notification.createWaterSoon(listOf("4,5,6"))),
                (11 to 2023) to listOf(Notification.createForgotToWater(listOf("1,2,3"))),
                (12 to 2023) to listOf(Notification.createWaterSoon(listOf("7,8"))),
            ).toSortedMap(
                compareByDescending<Pair<Int, Int>> { (_, year) -> year }
                    .thenByDescending { (day, _) -> day }
            )
        )

        override fun onFilterChanged(filter: NotificationFilter) = Unit
        override fun onNotificationClick(notification: Notification) = Unit
        override fun onBackClick() = Unit

    }

}