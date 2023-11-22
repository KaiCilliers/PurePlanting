package com.sunrisekcdeveloper.pureplanting.features.presentation.plantdetail

import com.sunrisekcdeveloper.pureplanting.features.component.plants.Plant
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantCache
import com.zhuinden.simplestack.Bundleable
import com.zhuinden.statebundle.StateBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlantDetailsViewModel(
    plant: Plant,
    private val plantCache: PlantCache,
): Bundleable {

    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
    var activePlant = MutableStateFlow(plant)

    fun waterPlant() = viewModelScope.launch {
        val watered = activePlant.value.water()
        plantCache.save(watered)
        activePlant.update { watered }
    }

    override fun toBundle(): StateBundle = StateBundle().apply {
        putParcelable("plant", activePlant.value)
    }

    override fun fromBundle(bundle: StateBundle?) {
        bundle?.run {
            getParcelable<Plant>("plant")?.let { savedPlant ->
                activePlant.update { savedPlant }
            }
        }
    }
}