package com.sunrisekcdeveloper.pureplanting.features.component.notifications

interface NotificationsCache {
    fun save(notification: NotificationDomain)
    fun all(): List<NotificationDomain>
    fun markAsSeen(id: String)
}

