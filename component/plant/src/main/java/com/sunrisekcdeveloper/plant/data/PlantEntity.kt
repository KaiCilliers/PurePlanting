package com.sunrisekcdeveloper.plant.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
data class PlantEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val size: String,
    val description: String,
    val imageSrcUri: String,
    val timeToWater: LocalTime,
    val daysToWater: List<DayOfWeek>,
    val amountToWaterWith: String,
    val createdAt: LocalDateTime,
)