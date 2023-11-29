package com.sunrisekcdeveloper.reminders

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface WateringWorkerResultStatusDao {
    @Insert
    fun insert(item: WateringWorkerResultStatusEntity)
}