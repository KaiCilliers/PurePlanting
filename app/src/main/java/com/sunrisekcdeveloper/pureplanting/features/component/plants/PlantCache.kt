package com.sunrisekcdeveloper.pureplanting.features.component.plants

import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface PlantCache {
    fun save(plant: Plant)
    fun remove(plantId: UUID)
    fun find(plantId: UUID): Plant?
    fun observe(): Flow<List<Plant>>
    fun allThatNeedsWateringSoon(): List<Plant>
}