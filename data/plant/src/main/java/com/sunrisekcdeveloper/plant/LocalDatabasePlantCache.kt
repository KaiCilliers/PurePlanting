package com.sunrisekcdeveloper.plant

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// todo rename
class LocalDatabasePlantCache(
    private val plantDao: PlantDao
) : PlantCache {

    override suspend fun save(plant: Plant) {
        plantDao.insert(plant)
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

