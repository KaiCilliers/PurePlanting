package com.sunrisekcdeveloper.pureplanting.features.component

import com.sunrisekcdeveloper.pureplanting.features.presentation.plants.Plant
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface PlantCache {
    fun save(plant: Plant)
    fun remove(plantId: UUID)
    fun find(plantId: UUID): Plant?
    fun observe(): Flow<List<Plant>>
    fun allThatNeedsWateringSoon(): List<Plant>
}