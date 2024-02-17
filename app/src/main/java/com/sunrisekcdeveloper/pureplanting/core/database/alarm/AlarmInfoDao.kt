package com.sunrisekcdeveloper.pureplanting.core.database.alarm

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.LocalDateTime

@Dao
interface AlarmInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg alarms: AlarmInfoEntity)

    @Query("SELECT * FROM AlarmInfoEntity")
    suspend fun allAlarms(): List<AlarmInfoEntity>

    @Query("DELETE FROM AlarmInfoEntity WHERE scheduledTime = :scheduledTime AND type = :type")
    suspend fun delete(scheduledTime: LocalDateTime, type: String)
}