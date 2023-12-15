package com.sunrisekcdeveloper.pureplanting.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sunrisekcdeveloper.notification.data.NotificationDao
import com.sunrisekcdeveloper.notification.data.NotificationEntity
import com.sunrisekcdeveloper.plant.data.PlantDao
import com.sunrisekcdeveloper.plant.data.PlantEntity
import com.sunrisekcdeveloper.plant.data.WateredRecordEntity
import com.sunrisekcdeveloper.pureplanting.workers.ForgotWaterWorkerResultStatusDao
import com.sunrisekcdeveloper.pureplanting.workers.ForgotWaterWorkerResultStatusEntity
import com.sunrisekcdeveloper.pureplanting.workers.WateringWorkerResultStatusDao
import com.sunrisekcdeveloper.pureplanting.workers.WateringWorkerResultStatusEntity

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