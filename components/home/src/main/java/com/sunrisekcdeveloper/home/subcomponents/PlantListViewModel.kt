package com.sunrisekcdeveloper.home.subcomponents

import com.sunrisekcdeveloper.design.ui.SnackbarEmitter
import com.sunrisekcdeveloper.design.ui.SnackbarEmitterType
import com.sunrisekcdeveloper.home.models.PlantTabFilter
import com.sunrisekcdeveloper.plant.Plant
import com.sunrisekcdeveloper.plant.PlantRepository
import com.zhuinden.simplestack.Bundleable
import com.zhuinden.statebundle.StateBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

interface PlantListViewModel {

    interface Router {
        fun goToAddPlant()
        fun goToPlantDetail(plant: Plant)
    }

    val filter: StateFlow<PlantTabFilter>

    val plants: StateFlow<List<Plant>>

    val isLoading: StateFlow<Boolean>

    fun onWaterPlant(plant: Plant)

    fun onUndoWater(plant: Plant)

    fun onFilterChange(filter: PlantTabFilter)

    fun onDeletePlant(plant: Plant)

    fun onUndoDelete()

    fun onPlantClick(plant: Plant)

    fun onAddPlantClick()

    class Fake : PlantListViewModel {
        override val filter: StateFlow<PlantTabFilter> = MutableStateFlow(PlantTabFilter.UPCOMING)

        override val isLoading: StateFlow<Boolean> = MutableStateFlow(true)

        override val plants: StateFlow<List<Plant>> = MutableStateFlow(
            listOf(
                Plant.createNewPlant(
                    imageSrc = "",
                    name = "My plant",
                    description = "This is a description which can span over two lines which is not what we want for the plant card",
                    size = "medium",
                    wateringDays = listOf(DayOfWeek.FRIDAY),
                    wateringTime = LocalTime.now(),
                    wateringAmount = "230ml",
                    createdAt = LocalDateTime.now().minusWeeks(2)
                ),
                Plant.createNewPlant(
                    imageSrc = "",
                    name = "My plant #2",
                    description = "short",
                    size = "medium",
                    wateringDays = listOf(DayOfWeek.MONDAY),
                    wateringTime = LocalTime.now(),
                    wateringAmount = "230ml"
                ),
                Plant.createNewPlant(
                    imageSrc = "",
                    name = "My plant #3",
                    description = "this is a description",
                    size = "medium",
                    wateringDays = listOf(DayOfWeek.MONDAY),
                    wateringTime = LocalTime.now(),
                    wateringAmount = "230ml"
                ),
                Plant.createNewPlant(
                    imageSrc = "",
                    name = "My plant #4",
                    description = "this is a description",
                    size = "medium",
                    wateringDays = listOf(DayOfWeek.MONDAY),
                    wateringTime = LocalTime.now(),
                    wateringAmount = "230ml"
                ),
                Plant.createNewPlant(
                    imageSrc = "",
                    name = "My plant #5",
                    description = "this is a description",
                    size = "medium",
                    wateringDays = listOf(DayOfWeek.MONDAY),
                    wateringTime = LocalTime.now(),
                    wateringAmount = "230ml"
                ),
                Plant.createNewPlant(
                    imageSrc = "",
                    name = "My plant #6",
                    description = "this is a description",
                    size = "medium",
                    wateringDays = listOf(DayOfWeek.MONDAY),
                    wateringTime = LocalTime.now(),
                    wateringAmount = "230ml"
                ),
                Plant.createNewPlant(
                    imageSrc = "",
                    name = "My plant #7",
                    description = "this is a description",
                    size = "medium",
                    wateringDays = listOf(DayOfWeek.MONDAY),
                    wateringTime = LocalTime.now(),
                    wateringAmount = "230ml"
                )
            )
        )

        override fun onWaterPlant(plant: Plant) = Unit

        override fun onUndoWater(plant: Plant) = Unit

        override fun onFilterChange(filter: PlantTabFilter) = Unit

        override fun onDeletePlant(plant: Plant) = Unit

        override fun onUndoDelete() = Unit

        override fun onPlantClick(plant: Plant) = Unit

        override fun onAddPlantClick() = Unit

    }

    class Default(
        private val plantRepository: PlantRepository,
        private val router: Router,
        private val eventEmitter: SnackbarEmitter,
        private val clock: Clock = Clock.systemDefaultZone(),
    ) : PlantListViewModel, Bundleable {

        private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

        private var recentlyDeletedPlant: Plant? = null

        override val filter = MutableStateFlow(PlantTabFilter.UPCOMING)

        override val isLoading = MutableStateFlow(true)

        override val plants = combine(plantRepository.observe(), filter) { allPlants, selectedFilter ->
            isLoading.value = false
            val filteredPlants = allPlants.filter {
                when (selectedFilter) {
                    PlantTabFilter.UPCOMING -> it.waterTimeIsUpcoming(LocalDateTime.now(clock))
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
                eventEmitter.emit(SnackbarEmitterType.Undo(
                    text = "Deleted plant \"${plant.details.name}\"",
                    undoAction = ::onUndoDelete
                ))
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

}