package com.sunrisekcdeveloper.db_tables.plant

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg plants: PlantEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg waterRecords: WateredRecordEntity)

    @Transaction
    suspend fun insert(plant: Pair<PlantEntity, List<WateredRecordEntity>>) {
        insert(plant.first)
        insert(*plant.second.toTypedArray())
    }

    @Transaction
    @Query("SELECT * FROM PlantEntity WHERE :plantId = id")
    suspend fun findById(plantId: String): PlantWithWaterRecords?

    @Query("DELETE FROM PlantEntity WHERE :plantId = id")
    suspend fun delete(plantId: String)

    @Transaction
    @Query("SELECT * FROM PlantEntity")
    suspend fun allPlantsWithWateringRecords(): List<PlantWithWaterRecords>

    @Transaction
    @Query("SELECT * FROM PlantEntity")
    fun observeAllPlantsWithWateringRecords(): Flow<List<PlantWithWaterRecords>>
}