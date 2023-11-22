package com.sunrisekcdeveloper.pureplanting.features.component

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationDao
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationEntity
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantDao
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantEntity
import com.sunrisekcdeveloper.pureplanting.features.component.plants.WateredRecordEntity

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
}