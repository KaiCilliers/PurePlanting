package com.sunrisekcdeveloper.pureplanting.library.db_tables

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