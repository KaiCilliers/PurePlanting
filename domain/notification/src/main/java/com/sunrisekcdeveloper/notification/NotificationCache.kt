package com.sunrisekcdeveloper.notification

import kotlinx.coroutines.flow.Flow

interface NotificationCache {
    suspend fun save(notification: NotificationDomain)
    fun observe(): Flow<List<NotificationDomain>>
    suspend fun all(): List<NotificationDomain>
    suspend fun markAsSeen(id: String)
}

