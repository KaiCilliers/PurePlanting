package com.sunrisekcdeveloper.notification

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sunrisekcdeveloper.notification.NotificationEntity
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

