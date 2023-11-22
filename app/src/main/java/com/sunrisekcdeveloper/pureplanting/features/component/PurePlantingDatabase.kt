package com.sunrisekcdeveloper.pureplanting.features.component

import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationDao
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationEntity
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantDao
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantEntity
import com.sunrisekcdeveloper.pureplanting.features.component.plants.WateredRecordEntity
import java.time.LocalDateTime

@Database(
    entities = [
        PlantEntity::class,
        WateredRecordEntity::class,
        NotificationEntity::class
    ],
    version = 1
)
@TypeConverters(RoomTypeConverters::class)
abstract class PurePlantingDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao
    abstract fun notificationDao(): NotificationDao
    abstract fun notificationDao2(): WateringWorkerResultStatusDao
}

interface WateringWorkerResultStatusDao {
    @Insert
    fun insert(item: WateringWorkerResultStatusEntity)
}

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