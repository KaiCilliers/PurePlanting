package com.sunrisekcdeveloper.reminders

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface WateringWorkerResultStatusDao {
    @Insert
    suspend fun insert(item: WateringWorkerResultStatusEntity)
}