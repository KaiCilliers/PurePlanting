package com.sunrisekcdeveloper.pureplanting.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class WateringWorkerResultStatusEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val status: String,
    val amountOfPlants: Int,
    val notificationId: String?,
    val atDate: LocalDateTime,
    val failureMsg: String? = null
)

@Entity
data class ForgotWaterWorkerResultStatusEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val status: String,
    val amountOfPlants: Int,
    val notificationId: String?,
    val atDate: LocalDateTime,
    val failureMsg: String? = null
)