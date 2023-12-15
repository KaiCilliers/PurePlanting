package com.sunrisekcdeveloper.notification.data

import com.sunrisekcdeveloper.notification.domain.Notification
import com.sunrisekcdeveloper.notification.domain.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultNotificationRepository(
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