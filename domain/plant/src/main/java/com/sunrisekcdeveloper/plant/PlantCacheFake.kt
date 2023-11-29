package com.sunrisekcdeveloper.plant

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class PlantCacheFake : com.sunrisekcdeveloper.plant.PlantCache {

    private val plants = MutableStateFlow<List<com.sunrisekcdeveloper.plant.Plant>>(emptyList())
    var throwException: Boolean = false

    override suspend fun save(plant: com.sunrisekcdeveloper.plant.Plant) {
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

    override suspend fun find(plantId: String): com.sunrisekcdeveloper.plant.Plant? {
        if (throwException) throw Exception("Forced test failure")
        return plants.value.find { it.id == plantId }
    }

    override fun observe(): Flow<List<com.sunrisekcdeveloper.plant.Plant>> {
        if (throwException) throw Exception("Forced test failure")
        return plants
    }

    fun resetData(plants: List<com.sunrisekcdeveloper.plant.Plant> = emptyList()) {
        if (throwException) throw Exception("Forced test failure")
        this.plants.value = plants
    }

    override suspend fun all(): List<com.sunrisekcdeveloper.plant.Plant> {
        if (throwException) throw Exception("Forced test failure")
        return plants.value
    }
}