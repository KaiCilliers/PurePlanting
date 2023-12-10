package com.sunrisekcdeveloper.plantList

import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import com.zhuinden.simplestack.Bundleable
import com.zhuinden.statebundle.StateBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDateTime

class DefaultPlantListComponent(
    private val plantRepository: PlantRepository,
    private val router: PlantListComponent.Router,
    private val clock: Clock = Clock.systemDefaultZone(),
) : PlantListComponent, Bundleable {

    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

    private var recentlyDeletedPlant: Plant? = null

    private val eventsChannel = Channel<PlantListComponent.Event>()
    override val eventsFlow = eventsChannel.receiveAsFlow()

    override val filter = MutableStateFlow(PlantTabFilter.UPCOMING)

    override val plants = combine(plantRepository.observe(), filter) { allPlants, selectedFilter ->
        val filteredPlants = allPlants.filter {
            when (selectedFilter) {
                PlantTabFilter.UPCOMING -> it.needsWaterToday(LocalDateTime.now(clock))
                PlantTabFilter.FORGOT_TO_WATER -> it.missedLatestWateringDate(LocalDateTime.now(clock))
                PlantTabFilter.HISTORY -> it.dateLastWatered != null
            }
        }

        val sortedPlants = when (selectedFilter) {
            PlantTabFilter.UPCOMING -> filteredPlants.sortedBy { it.wateringInfo.time }
            PlantTabFilter.FORGOT_TO_WATER -> filteredPlants.sortedBy { it.wateringInfo.time }
            PlantTabFilter.HISTORY -> filteredPlants.sortedByDescending { it.dateLastWatered ?: LocalDateTime.now(clock) }
        }

        sortedPlants
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyList())

    override fun onWaterPlant(plant: Plant) {
        viewModelScope.launch {
            plantRepository.save(plant.water(clock))
        }
    }

    override fun onUndoWater(plant: Plant) {
        viewModelScope.launch {
            plantRepository.save(plant.undoWatering())
        }
    }

    override fun onFilterChange(filter: PlantTabFilter) {
        this.filter.value = filter
    }

    override fun onDeletePlant(plant: Plant) {
        viewModelScope.launch {
            plantRepository.remove(plant.id)
            recentlyDeletedPlant = plant
            eventsChannel.send(PlantListComponent.Event.DeletedPlant)
        }
    }

    override fun onUndoDelete() {
        recentlyDeletedPlant?.let {
            viewModelScope.launch { plantRepository.save(it) }
        }
    }

    override fun onPlantClick(plant: Plant) {
        router.goToPlantDetail(plant)
    }

    override fun onAddPlantClick() {
        router.goToAddPlant()
    }

    override fun toBundle(): StateBundle = StateBundle().apply {
        putString("activeFilter", filter.value.toString())
    }

    override fun fromBundle(bundle: StateBundle?) {
        bundle?.run {
            filter.value = PlantTabFilter.valueOf(getString("activeFilter", PlantTabFilter.UPCOMING.toString()))
        }
    }
}