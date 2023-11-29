package com.sunrisekcdeveloper.plantDetail

import com.sunrisekcdeveloper.android.ParcelablePlant
import com.sunrisekcdeveloper.android.toDomain
import com.sunrisekcdeveloper.android.toParcelable
import com.sunrisekcdeveloper.plant.PlantCache
import com.zhuinden.simplestack.Bundleable
import com.zhuinden.statebundle.StateBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlantDetailsViewModel(
    plant: ParcelablePlant,
    private val plantCache: PlantCache,
): Bundleable {

    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
    var activePlant = MutableStateFlow(plant.toDomain())

    fun waterPlant() = viewModelScope.launch {
        val watered = activePlant.value.water()
        plantCache.save(watered)
        activePlant.update { watered }
    }

    override fun toBundle(): StateBundle = StateBundle().apply {
        putParcelable("plant", activePlant.value.toParcelable())
    }

    override fun fromBundle(bundle: StateBundle?) {
        bundle?.run {
            getParcelable<ParcelablePlant>("plant")?.let { savedPlant ->
                activePlant.update { savedPlant.toDomain() }
            }
        }
    }
}