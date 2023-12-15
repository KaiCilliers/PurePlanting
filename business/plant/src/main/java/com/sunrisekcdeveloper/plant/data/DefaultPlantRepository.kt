package com.sunrisekcdeveloper.plant.data

import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultPlantRepository(
    private val plantDao: PlantDao
) : PlantRepository {

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

