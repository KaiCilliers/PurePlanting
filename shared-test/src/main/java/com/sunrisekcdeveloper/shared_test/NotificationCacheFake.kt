package com.sunrisekcdeveloper.shared_test

import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationDomain
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationsCache

class NotificationCacheFake : NotificationsCache {

    private val notifications: MutableList<NotificationDomain> = mutableListOf()

    fun resetData(notifications: List<NotificationDomain> = emptyList()) {
        this.notifications.clear()
        this.notifications.addAll(notifications)
    }

    override fun save(notification: NotificationDomain) {
        notifications.add(notification)
    }

    override fun all(): List<NotificationDomain> {
        return notifications
    }

    override fun markAsSeen(id: String) {
        val existingItemIndex = notifications.indexOfFirst { item -> item.id == id }
        if (existingItemIndex >= 0) {
            notifications[existingItemIndex] = notifications[existingItemIndex].copy(seen = true)
        }
    }
}