package com.sunrisekcdeveloper.pureplanting.features.component.plants

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        PlantEntity::class,
        WateredRecordEntity::class
    ],
    version = 1
)
@TypeConverters(RoomTypeConverters::class)
abstract class PurePlantingDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao
}