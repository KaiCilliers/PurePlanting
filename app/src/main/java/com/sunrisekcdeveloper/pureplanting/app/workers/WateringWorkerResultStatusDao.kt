package com.sunrisekcdeveloper.pureplanting.app.workers

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface WateringWorkerResultStatusDao {
    @Insert
    suspend fun insert(item: WateringWorkerResultStatusEntity)
}

@Dao
interface ForgotWaterWorkerResultStatusDao {
    @Insert
    suspend fun insert(item: ForgotWaterWorkerResultStatusEntity)
}