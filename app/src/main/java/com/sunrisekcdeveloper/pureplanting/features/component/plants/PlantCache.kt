package com.sunrisekcdeveloper.pureplanting.features.component.plants

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface PlantCache {
    suspend fun save(plant: Plant)
    suspend fun remove(plantId: String)
    suspend fun find(plantId: String): Plant?
    fun observe(): Flow<List<Plant>>
    suspend fun all(): List<Plant>

    class Smart(
        private val origin: PlantCache
    ) {
        /**
         * Returns plants without a record of being watered today that need water before [deadline]
         */
        suspend fun allThatNeedsWateringBefore(deadline: LocalDateTime): List<Plant> {
            return origin
                .all()
                .filter { it.needsWater(deadline) }
                .filter { it.wateringInfo.time <= deadline.toLocalTime()}
        }
    }
}