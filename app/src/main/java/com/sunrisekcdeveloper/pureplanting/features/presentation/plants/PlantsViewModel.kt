package com.sunrisekcdeveloper.pureplanting.features.presentation.plants

import com.sunrisekcdeveloper.pureplanting.features.component.plants.Plant
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantCache
import com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant.AddEditPlantKey
import com.sunrisekcdeveloper.pureplanting.features.presentation.notifications.NotificationsKey
import com.sunrisekcdeveloper.pureplanting.features.presentation.plantdetail.PlantDetailKey
import com.zhuinden.flowcombinetuplekt.combineTuple
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestack.Bundleable
import com.zhuinden.statebundle.StateBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID

class PlantsViewModel(
    private val plantCache: PlantCache,
    private val clock: Clock = Clock.systemDefaultZone(),
    private val backstack: Backstack,
) : Bundleable {
    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
    private var lastRemovedPlant: Plant? = null

    private val allPlants: StateFlow<List<Plant>> = plantCache
        .observe()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyList())
    val activeFilter = MutableStateFlow(PlantListFilter.UPCOMING)

    val plants: StateFlow<List<Plant>> = combineTuple(allPlants, activeFilter).map { (plants, filter) ->
        plants.filter { plant ->
            when (filter) {
                PlantListFilter.UPCOMING -> plant.needsWaterSoon(LocalDateTime.now(clock))
                PlantListFilter.FORGOT_TO_WATER -> plant.forgotToWater(LocalDateTime.now(clock))
                PlantListFilter.HISTORY -> plant.wateringInfo.previousWaterDates.isNotEmpty()
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyList())

    fun setFilter(filter: PlantListFilter) {
        activeFilter.update { filter }
    }

    fun removePlant(plantId: UUID) {
        lastRemovedPlant = plantCache.find(plantId)
        plantCache.remove(plantId)
    }

    fun undoRemove(plantId: UUID) {
        plantCache.save(lastRemovedPlant!!)
    }

    fun navigateToNotification() {
        backstack.goTo(NotificationsKey)
    }

    fun navigateToAddPlant() {
        backstack.goTo(AddEditPlantKey)
    }

    fun navigateToPlantDetail(plant: Plant) {
        backstack.goTo(PlantDetailKey)
    }

    override fun toBundle(): StateBundle = StateBundle().apply {
        putString("activeFilter", activeFilter.value.toString())
    }

    override fun fromBundle(bundle: StateBundle?) {
        bundle?.run {
            activeFilter.update { PlantListFilter.valueOf(getString("activeFilter", PlantListFilter.UPCOMING.toString())) }
        }
    }
}

enum class PlantListFilter {
    UPCOMING, FORGOT_TO_WATER, HISTORY
}