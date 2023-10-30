package com.sunrisekcdeveloper.pureplanting.features.presentation.plantdetail

import com.sunrisekcdeveloper.pureplanting.features.component.plants.Plant
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantCache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class PlantDetailsViewModel(
    private val plant: Plant,
    private val plantCache: PlantCache,
) {

    var activePlant = MutableStateFlow(plant)

    fun waterPlant(plant: Plant) {
        val watered = plant.water()
        plantCache.save(watered)
        activePlant.update { watered }
    }
}