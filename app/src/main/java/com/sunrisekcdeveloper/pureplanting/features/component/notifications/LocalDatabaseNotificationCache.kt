package com.sunrisekcdeveloper.pureplanting.features.component.notifications

import com.sunrisekcdeveloper.pureplanting.features.component.PurePlantingDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDatabaseNotificationCache(
    private val db: PurePlantingDatabase
): NotificationCache {

    private val notificationDao by lazy { db.notificationDao() }

    override suspend fun save(notification: NotificationDomain) {
        notificationDao.insert(notification.toEntity())
    }

    override fun observe(): Flow<List<NotificationDomain>> {
        return notificationDao
            .observe()
            .map { list -> list.map { it.toNotification() } }
    }

    override suspend fun all(): List<NotificationDomain> {
        return notificationDao.allNotifications().map { it.toNotification() }
    }

    override suspend fun markAsSeen(id: String) {
        notificationDao.markAsSeen(id)
    }

}