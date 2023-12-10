package com.sunrisekcdeveloper.plant.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
}