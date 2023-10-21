package com.sunrisekcdeveloper.pureplanting

import java.util.UUID

interface PlantCache {
    fun add(plant: Plant)
    fun remove(plantId: UUID)
    fun load(plantId: UUID)
}

