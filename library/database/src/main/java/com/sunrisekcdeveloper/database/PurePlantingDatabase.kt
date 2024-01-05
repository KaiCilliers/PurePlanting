package com.sunrisekcdeveloper.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sunrisekcdeveloper.db_tables.ForgotWaterWorkerResultStatusDao
import com.sunrisekcdeveloper.db_tables.ForgotWaterWorkerResultStatusEntity
import com.sunrisekcdeveloper.db_tables.WateringWorkerResultStatusDao
import com.sunrisekcdeveloper.db_tables.WateringWorkerResultStatusEntity
import com.sunrisekcdeveloper.db_tables.notification.NotificationDao
import com.sunrisekcdeveloper.db_tables.notification.NotificationEntity
import com.sunrisekcdeveloper.db_tables.plant.PlantDao
import com.sunrisekcdeveloper.db_tables.plant.PlantEntity
import com.sunrisekcdeveloper.db_tables.plant.WateredRecordEntity

@Database(
    entities = [
        PlantEntity::class,
        WateredRecordEntity::class,
        NotificationEntity::class,
        WateringWorkerResultStatusEntity::class,
        ForgotWaterWorkerResultStatusEntity::class,
    ],
    version = 1
)
@TypeConverters(RoomTypeConverters::class)
abstract class PurePlantingDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao
    abstract fun notificationDao(): NotificationDao
    abstract fun waterWorkerDao(): WateringWorkerResultStatusDao
    abstract fun forgotWaterWorkerDao(): ForgotWaterWorkerResultStatusDao

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