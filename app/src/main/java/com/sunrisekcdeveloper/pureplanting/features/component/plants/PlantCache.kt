package com.sunrisekcdeveloper.pureplanting.features.component.plants

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.util.UUID

interface PlantCache {
    fun save(plant: Plant)
    fun remove(plantId: UUID)
    fun find(plantId: UUID): Plant?
    fun observe(): Flow<List<Plant>>
    fun all(): List<Plant>

    class Smart(
        private val origin: PlantCache
    ) {
        fun allThatNeedsWateringSoon(now: LocalDateTime): List<Plant> {
            return origin.all().filter { it.needsWaterSoon(now) }
        }
    }
}