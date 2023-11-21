package com.sunrisekcdeveloper.pureplanting.features.component.plants

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface PlantCache {
    fun save(plant: Plant)
    fun remove(plantId: String)
    fun find(plantId: String): Plant?
    fun observe(): Flow<List<Plant>>
    fun all(): List<Plant>

    class Smart(
        private val origin: PlantCache
    ) {
        /**
         * Returns plants without a record of being watered today that need water before [deadline]
         */
        fun allThatNeedsWateringBefore(deadline: LocalDateTime): List<Plant> {
            return origin
                .all()
                .filter { it.needsWater(deadline) }
                .filter { it.wateringInfo.time <= deadline.toLocalTime()}
        }
    }
}