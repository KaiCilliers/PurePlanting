package com.sunrisekcdeveloper.pureplanting.features.component.notifications

import kotlinx.coroutines.flow.Flow

interface NotificationCache {
    suspend fun save(notification: NotificationDomain)
    fun observe(): Flow<List<NotificationDomain>>
    suspend fun all(): List<NotificationDomain>
    suspend fun markAsSeen(id: String)
}

