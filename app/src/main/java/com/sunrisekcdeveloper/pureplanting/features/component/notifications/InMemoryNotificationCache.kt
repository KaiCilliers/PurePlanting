package com.sunrisekcdeveloper.pureplanting.features.component.notifications

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class InMemoryNotificationCache : NotificationCache {

    private val notifications = MutableStateFlow<List<NotificationDomain>>(emptyList())

    override suspend fun save(notification: NotificationDomain) {
        notifications.update {
            it.toMutableList().apply {
                val existingIndex = this.map { it.id }.indexOf(notification.id)
                if (existingIndex >= 0) this[existingIndex] = notification
                else add(notification)
            }
        }
    }

    override suspend fun all(): List<NotificationDomain> {
        return notifications.value
    }

    override suspend fun markAsSeen(id: String) {
        notifications.update {
            it.toMutableList().apply {
                val existingItem = this.first { it.id == id }
                val existingIndex = this.map { it.id }.indexOf(id)
                if (existingIndex >= 0) this[existingIndex] = existingItem.copy(seen = true)
            }
        }
    }

    override fun observe(): Flow<List<NotificationDomain>> = notifications
}