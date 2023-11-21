package com.sunrisekcdeveloper.pureplanting.features.component.plants

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class InMemoryPlantCache : PlantCache {
    // todo increased efficiency bt rather using a map via .associateBy
    private val plants = MutableStateFlow<List<Plant>>(emptyList())

    override fun save(plant: Plant) {
        plants.update {
            it.toMutableList().apply {
                val existingIndex = this.map { it.id }.indexOf(plant.id)
                if (existingIndex >= 0) this[existingIndex] = plant
                else add(plant)
            }
        }
    }

    override fun remove(plantId: String) {
        plants.update {
            it.toMutableList().apply {
                removeIf { it.id == plantId }
            }
        }
    }

    override fun find(plantId: String): Plant? {
        return plants.value.find { it.id == plantId }
    }

    override fun observe(): Flow<List<Plant>> {
        return plants
    }

    override fun all(): List<Plant> {
        return plants.value
    }
}