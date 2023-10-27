package com.sunrisekcdeveloper.shared_test

import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationDomain
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationsCache
import java.lang.Exception

class NotificationCacheFake : NotificationsCache {

    private val notifications: MutableList<NotificationDomain> = mutableListOf()
    var throwException: Boolean = false

    fun resetData(notifications: List<NotificationDomain> = emptyList()) {
        this.notifications.clear()
        this.notifications.addAll(notifications)
    }

    override fun save(notification: NotificationDomain) {
        if (throwException) throw Exception("Forced test failure")
        notifications.add(notification)
    }

    override fun all(): List<NotificationDomain> {
        if (throwException) throw Exception("Forced test failure")
        return notifications
    }

    override fun markAsSeen(id: String) {
        if (throwException) throw Exception("Forced test failure")
        val existingItemIndex = notifications.indexOfFirst { item -> item.id == id }
        if (existingItemIndex >= 0) {
            notifications[existingItemIndex] = notifications[existingItemIndex].copy(seen = true)
        }
    }
}