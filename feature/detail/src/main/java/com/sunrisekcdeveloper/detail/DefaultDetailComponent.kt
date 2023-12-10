package com.sunrisekcdeveloper.detail

import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import com.zhuinden.simplestack.Bundleable
import com.zhuinden.statebundle.StateBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DefaultDetailComponent(
    plant: Plant,
    private val plantRepository: PlantRepository,
    private val router: Router
): DetailComponent, Bundleable {

    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

    override val plant = MutableStateFlow(plant)

    override fun onWaterPlant() {
        viewModelScope.launch {
            val watered = plant.value.water()
            plantRepository.save(watered)
            plant.update { watered }
        }
    }

    override fun onEditPlant() {
        router.goToEditPlant(plant.value)
    }

    override fun toBundle(): StateBundle = StateBundle().apply {
        putParcelable("plant", plant.value)
    }

    override fun fromBundle(bundle: StateBundle?) {
        bundle?.run {
            getParcelable<Plant>("plant")?.let { savedPlant ->
                plant.update { savedPlant }
            }
        }
    }
}