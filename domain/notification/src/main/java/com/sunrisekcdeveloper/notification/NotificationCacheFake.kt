package com.sunrisekcdeveloper.notification

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.lang.Exception

class NotificationCacheFake : NotificationCache {

    private val notifications = MutableStateFlow<List<NotificationDomain>>(emptyList())
    var throwException: Boolean = false

    fun resetData(notifications: List<NotificationDomain> = emptyList()) {
        this.notifications.value = notifications
    }

    override suspend fun save(notification: NotificationDomain) {
        if (throwException) throw Exception("Forced test failure")
        notifications.update {
            it.toMutableList().apply {
                val existingIndex = this.map { it.id }.indexOf(notification.id)
                if (existingIndex >= 0) this[existingIndex] = notification
                else add(notification)
            }
        }
    }

    override suspend fun all(): List<NotificationDomain> {
        if (throwException) throw Exception("Forced test failure")
        return notifications.value
    }

    override suspend fun markAsSeen(id: String) {
        if (throwException) throw Exception("Forced test failure")
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