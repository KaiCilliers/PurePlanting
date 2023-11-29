package com.sunrisekcdeveloper.pureplanting.app

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sunrisekcdeveloper.notification.NotificationDao
import com.sunrisekcdeveloper.notification.NotificationEntity
import com.sunrisekcdeveloper.plant.PlantDao
import com.sunrisekcdeveloper.plant.PlantEntity
import com.sunrisekcdeveloper.plant.WateredRecordEntity
import com.sunrisekcdeveloper.reminders.WateringWorkerResultStatusDao
import com.sunrisekcdeveloper.reminders.WateringWorkerResultStatusEntity
import java.time.LocalDateTime

@Database(
    entities = [
        PlantEntity::class,
        WateredRecordEntity::class,
        NotificationEntity::class,
        WateringWorkerResultStatusEntity::class,
    ],
    version = 1
)
// todo continue to extract DB local to module :data:plant etc
@TypeConverters(RoomTypeConverters::class)
abstract class PurePlantingDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao
    abstract fun notificationDao(): NotificationDao
    abstract fun notificationDao2(): WateringWorkerResultStatusDao

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