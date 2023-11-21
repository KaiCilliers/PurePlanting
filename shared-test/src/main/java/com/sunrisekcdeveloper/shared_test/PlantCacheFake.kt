package com.sunrisekcdeveloper.shared_test

import com.sunrisekcdeveloper.pureplanting.features.component.plants.Plant
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class PlantCacheFake : PlantCache {

    private val plants = MutableStateFlow<List<Plant>>(emptyList())
    var throwException: Boolean = false

    override fun save(plant: Plant) {
        if (throwException) throw Exception("Forced test failure")
        plants.update {
            it.toMutableList().apply {
                val existingIndex = this.map { it.id }.indexOf(plant.id)
                if (existingIndex >= 0) this[existingIndex] = plant
                else add(plant)
            }
        }
    }

    override fun remove(plantId: String) {
        if (throwException) throw Exception("Forced test failure")
        plants.update {
            it.toMutableList().apply {
                removeIf { it.id == plantId }
            }
        }
    }

    override fun find(plantId: String): Plant? {
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

    override fun all(): List<Plant> {
        if (throwException) throw Exception("Forced test failure")
        return plants.value
    }
}