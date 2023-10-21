package com.sunrisekcdeveloper.pureplanting

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class PlantCacheFake : PlantCache {

    private val plants = MutableStateFlow<List<Plant>>(emptyList())

    override fun save(plant: Plant) {
        plants.update {
            it.toMutableList().apply {
                add(plant)
            }
        }
    }

    override fun remove(plantId: UUID) {
        plants.update {
            it.toMutableList().apply {
                removeIf { it.id == plantId }
            }
        }
    }

    override fun find(plantId: UUID): Plant? {
        return plants.value.find { it.id == plantId }
    }

    override fun observe(): Flow<List<Plant>> = plants

    fun resetData(plants: List<Plant> = emptyList()) {
        this.plants.value = plants
    }
}