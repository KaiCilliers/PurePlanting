package com.sunrisekcdeveloper.pureplanting.library.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        com.sunrisekcdeveloper.pureplanting.library.db_tables.plant.PlantEntity::class,
        com.sunrisekcdeveloper.pureplanting.library.db_tables.plant.WateredRecordEntity::class,
        com.sunrisekcdeveloper.pureplanting.library.db_tables.notification.NotificationEntity::class,
        com.sunrisekcdeveloper.pureplanting.library.db_tables.WateringWorkerResultStatusEntity::class,
        com.sunrisekcdeveloper.pureplanting.library.db_tables.ForgotWaterWorkerResultStatusEntity::class,
    ],
    version = 1
)
@TypeConverters(RoomTypeConverters::class)
abstract class PurePlantingDatabase : RoomDatabase() {
    abstract fun plantDao(): com.sunrisekcdeveloper.pureplanting.library.db_tables.plant.PlantDao
    abstract fun notificationDao(): com.sunrisekcdeveloper.pureplanting.library.db_tables.notification.NotificationDao
    abstract fun waterWorkerDao(): com.sunrisekcdeveloper.pureplanting.library.db_tables.WateringWorkerResultStatusDao
    abstract fun forgotWaterWorkerDao(): com.sunrisekcdeveloper.pureplanting.library.db_tables.ForgotWaterWorkerResultStatusDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PurePlantingDatabase? = null

        fun getDatabase(context: Context): PurePlantingDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PurePlantingDatabase::class.java,
                    "pureplanting_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}