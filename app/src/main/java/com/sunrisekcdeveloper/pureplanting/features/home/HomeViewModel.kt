package com.sunrisekcdeveloper.pureplanting.features.home

import com.sunrisekcdeveloper.pureplanting.core.design.ui.SnackbarEmitter
import com.sunrisekcdeveloper.pureplanting.core.design.ui.SnackbarEmitterType
import com.sunrisekcdeveloper.pureplanting.domain.notification.NotificationRepository
import com.sunrisekcdeveloper.pureplanting.domain.plant.Plant
import com.sunrisekcdeveloper.pureplanting.domain.plant.PlantRepository
import com.sunrisekcdeveloper.pureplanting.features.home.models.PlantTabFilter
import com.zhuinden.simplestack.Bundleable
import com.zhuinden.statebundle.StateBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

interface HomeViewModel {

    interface Router {
        fun goToAddPlant()
        fun goToPlantDetail(plant: Plant)
        fun goToNotificationList()
    }

    val filter: StateFlow<PlantTabFilter>

    val plants: StateFlow<List<Plant>>

    val isNotificationBadgeVisible: StateFlow<Boolean>

    val isLoading: StateFlow<Boolean>

    fun onWaterPlant(plant: Plant)

    fun onUndoWater(plant: Plant)

    fun onFilterChange(filter: PlantTabFilter)

    fun onDeletePlant(plant: Plant)

    fun onUndoDelete()

    fun onPlantClick(plant: Plant)

    fun onAddPlantClick()

    fun onNotificationIconClick()

    class Default(
        private val plantRepository: PlantRepository,
        private val notificationRepository: NotificationRepository,
        private val router: Router,
        private val clock: Clock = Clock.systemDefaultZone(),
        private val eventEmitter: SnackbarEmitter,
    ) : HomeViewModel, Bundleable {

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

        override val isNotificationBadgeVisible = notificationRepository
            .observe()
            .map { allNotifications -> allNotifications.any { !it.seen } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), false)

        override fun onNotificationIconClick() {
            router.goToNotificationList()
        }

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
                eventEmitter.emit(
                    SnackbarEmitterType.Undo(
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

    class Fake : HomeViewModel {
        override val filter: StateFlow<PlantTabFilter> = MutableStateFlow(PlantTabFilter.UPCOMING)

        override val isLoading: StateFlow<Boolean> = MutableStateFlow(false)

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

        override val isNotificationBadgeVisible = MutableStateFlow(true)

        override fun onNotificationIconClick() = Unit

        override fun onWaterPlant(plant: Plant) = Unit

        override fun onUndoWater(plant: Plant) = Unit

        override fun onFilterChange(filter: PlantTabFilter) = Unit

        override fun onDeletePlant(plant: Plant) = Unit

        override fun onUndoDelete() = Unit

        override fun onPlantClick(plant: Plant) = Unit

        override fun onAddPlantClick() = Unit

    }

}