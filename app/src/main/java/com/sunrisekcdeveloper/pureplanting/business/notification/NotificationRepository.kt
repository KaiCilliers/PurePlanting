package com.sunrisekcdeveloper.pureplanting.business.notification

import com.sunrisekcdeveloper.pureplanting.library.db_tables.notification.NotificationDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.lang.Exception

interface NotificationRepository {
    suspend fun save(notification: Notification)
    fun observe(): Flow<List<Notification>>
    suspend fun all(): List<Notification>
    suspend fun markAsSeen(id: String)

    class Fake : NotificationRepository {

        private val notifications = MutableStateFlow<List<Notification>>(emptyList())
        var throwException: Boolean = false

        fun resetData(notifications: List<Notification> = emptyList()) {
            this.notifications.value = notifications
        }

        override suspend fun save(notification: Notification) {
            if (throwException) throw Exception("Forced test failure")
            notifications.update {
                it.toMutableList().apply {
                    val existingIndex = this.map { it.id }.indexOf(notification.id)
                    if (existingIndex >= 0) this[existingIndex] = notification
                    else add(notification)
                }
            }
        }

        override suspend fun all(): List<Notification> {
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

        override fun observe(): Flow<List<Notification>> = notifications
    }

    class Default(
        private val notificationDao: NotificationDao
    ): NotificationRepository {

        override suspend fun save(notification: Notification) {
            notificationDao.insert(notification.toEntity())
        }

        override fun observe(): Flow<List<Notification>> {
            return notificationDao
                .observe()
                .map { list -> list.map { it.toNotification() } }
        }

        override suspend fun all(): List<Notification> {
            return notificationDao.allNotifications().map { it.toNotification() }
        }

        override suspend fun markAsSeen(id: String) {
            notificationDao.markAsSeen(id)
        }

    }
}

