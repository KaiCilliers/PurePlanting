package com.sunrisekcdeveloper.pureplanting.features.component.plants

import androidx.room.withTransaction
import com.sunrisekcdeveloper.pureplanting.features.component.PurePlantingDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// todo rename
class LocalDatabasePlantCache(
    private val db: PurePlantingDatabase
) : PlantCache {

    private val plantDao by lazy { db.plantDao() }

    override suspend fun save(plant: Plant) {
        db.withTransaction {
            plantDao.insert(plant.toEntity())
            plantDao.insert(*plant.toWaterRecordsEntity().toTypedArray())
        }
    }

    override suspend fun remove(plantId: String) {
        plantDao.delete(plantId)
    }

    override suspend fun find(plantId: String): Plant? {
        return plantDao.findById(plantId)?.toPlant()
    }

    override fun observe(): Flow<List<Plant>> {
        return plantDao
            .observeAllPlantsWithWateringRecords()
            .map { list -> list.map { it.toPlant() } }
    }

    override suspend fun all(): List<Plant> {
        return plantDao
            .allPlantsWithWateringRecords()
            .map { it.toPlant() }
    }
}

