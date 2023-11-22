package com.sunrisekcdeveloper.pureplanting.features.component.notifications

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg notifications: NotificationEntity)

    @Query("SELECT * FROM NotificationEntity")
    suspend fun allNotifications(): List<NotificationEntity>

    @Query("""
        UPDATE NotificationEntity
        SET seen = "true"        
        WHERE :id = id 
    """)
    suspend fun markAsSeen(id: String)

    @Query("SELECT * FROM NotificationEntity")
    fun observe(): Flow<List<NotificationEntity>>

}

