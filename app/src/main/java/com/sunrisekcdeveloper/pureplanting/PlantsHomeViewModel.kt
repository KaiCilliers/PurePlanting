package com.sunrisekcdeveloper.pureplanting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhuinden.flowcombinetuplekt.combineTuple
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.Clock
import java.time.LocalDateTime

class PlantsHomeViewModel(
    plantCache: PlantCache,
    private val clock: Clock = Clock.systemDefaultZone(),
): ViewModel() {

    private val allPlants: StateFlow<List<Plant>> = plantCache
        .observe()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyList())
    private val activeFilter = MutableStateFlow(PlantFilter.UPCOMING)

    val plants: StateFlow<List<Plant>> = combineTuple(allPlants, activeFilter).map { (plants, filter) ->
        plants.filter { plant ->
            when (filter) {
                PlantFilter.UPCOMING -> plant.needsWaterSoon(LocalDateTime.now(clock))
                PlantFilter.FORGOT -> plant.forgotToWater(LocalDateTime.now(clock))
                PlantFilter.HISTORY -> plant.wateringInfo.previousWaterDates.isNotEmpty()
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyList())

    fun setFilter(filter: PlantFilter) {
        activeFilter.update { filter }
    }
}

enum class PlantFilter {
    UPCOMING, FORGOT, HISTORY
}