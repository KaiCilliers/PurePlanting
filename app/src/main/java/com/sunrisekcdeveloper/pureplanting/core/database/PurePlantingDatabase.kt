package com.sunrisekcdeveloper.pureplanting.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sunrisekcdeveloper.pureplanting.core.database.alarm.AlarmInfoDao
import com.sunrisekcdeveloper.pureplanting.core.database.alarm.AlarmInfoEntity

@Database(
    entities = [
        com.sunrisekcdeveloper.pureplanting.library.db_tables.plant.PlantEntity::class,
        com.sunrisekcdeveloper.pureplanting.library.db_tables.plant.WateredRecordEntity::class,
        com.sunrisekcdeveloper.pureplanting.library.db_tables.notification.NotificationEntity::class,
        WateringWorkerResultStatusEntity::class,
        ForgotWaterWorkerResultStatusEntity::class,
        AlarmInfoEntity::class,
    ],
    version = 2
)
@TypeConverters(RoomTypeConverters::class)
abstract class PurePlantingDatabase : RoomDatabase() {
    abstract fun plantDao(): com.sunrisekcdeveloper.pureplanting.library.db_tables.plant.PlantDao
    abstract fun notificationDao(): com.sunrisekcdeveloper.pureplanting.library.db_tables.notification.NotificationDao
    abstract fun waterWorkerDao(): WateringWorkerResultStatusDao
    abstract fun forgotWaterWorkerDao(): ForgotWaterWorkerResultStatusDao
    abstract fun alarmInfoDao(): AlarmInfoDao

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
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE `AlarmInfoEntity` (
                `createdAt` TEXT NOT NULL,
                `scheduledTime` TEXT NOT NULL PRIMARY KEY,
                `type` TEXT NOT NULL,
                `repeatingInterval` INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}