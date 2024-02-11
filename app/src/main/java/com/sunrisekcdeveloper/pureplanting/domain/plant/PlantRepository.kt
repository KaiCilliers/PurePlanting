package com.sunrisekcdeveloper.pureplanting.domain.plant

import com.sunrisekcdeveloper.pureplanting.library.db_tables.plant.PlantDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

interface PlantRepository {
    suspend fun save(plant: Plant)
    suspend fun remove(plantId: String)
    suspend fun find(plantId: String): Plant?
    fun observe(): Flow<List<Plant>>
    suspend fun all(): List<Plant>

    class Fake : PlantRepository {

        private val plants = MutableStateFlow<List<Plant>>(emptyList())
        var throwException: Boolean = false

        override suspend fun save(plant: Plant) {
            if (throwException) throw Exception("Forced test failure")
            plants.update {
                it.toMutableList().apply {
                    val existingIndex = this.map { it.id }.indexOf(plant.id)
                    if (existingIndex >= 0) this[existingIndex] = plant
                    else add(plant)
                }
            }
        }

        override suspend fun remove(plantId: String) {
            if (throwException) throw Exception("Forced test failure")
            plants.update {
                it.toMutableList().apply {
                    removeIf { it.id == plantId }
                }
            }
        }

        override suspend fun find(plantId: String): Plant? {
            if (throwException) throw Exception("Forced test failure")
            return plants.value.find { it.id == plantId }
        }

        override fun observe(): Flow<List<Plant>> {
            if (throwException) throw Exception("Forced test failure")
            return plants
        }

        fun resetData(plants: List<Plant> = emptyList()) {
            if (throwException) throw Exception("Forced test failure")
            this.plants.value = plants
        }

        override suspend fun all(): List<Plant> {
            if (throwException) throw Exception("Forced test failure")
            return plants.value
        }
    }

    class Default(
        private val plantDao: PlantDao
    ) : PlantRepository {
        override suspend fun save(plant: Plant) {
            val entity = plant.toEntity()
            val waterRecords = plant.toWaterRecordsEntity()
            plantDao.insert(Pair(entity, waterRecords))
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
}