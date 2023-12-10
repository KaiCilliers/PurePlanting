package com.sunrisekcdeveloper.plantList

import com.sunrisekcdeveloper.plant.domain.Plant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import java.time.DayOfWeek
import java.time.LocalTime

interface PlantListComponent {

    interface Router {
        fun goToAddPlant()
        fun goToPlantDetail(plant: Plant)
    }

    val filter: StateFlow<PlantTabFilter>

    val plants: StateFlow<List<Plant>>

    val eventsFlow: Flow<Event>

    fun onWaterPlant(plant: Plant)

    fun onUndoWater(plant: Plant)

    fun onFilterChange(filter: PlantTabFilter)

    fun onDeletePlant(plant: Plant)

    fun onUndoDelete()

    fun onPlantClick(plant: Plant)

    fun onAddPlantClick()

    sealed class Event {
        data object DeletedPlant : Event()
        data object EditedPlant : Event()
    }

    class Fake : PlantListComponent {
        override val filter: StateFlow<PlantTabFilter> = MutableStateFlow(PlantTabFilter.UPCOMING)

        override val plants: StateFlow<List<Plant>> = MutableStateFlow(
            listOf(
                Plant.createNewPlant(
                    imageSrc = "",
                    name = "My plant",
                    description = "this is a description",
                    size = "medium",
                    wateringDays = listOf(DayOfWeek.MONDAY),
                    wateringTime = LocalTime.now(),
                    wateringAmount = "230ml"
                )
            )
        )

        override val eventsFlow: Flow<Event> = emptyFlow()

        override fun onWaterPlant(plant: Plant) = Unit

        override fun onUndoWater(plant: Plant) = Unit

        override fun onFilterChange(filter: PlantTabFilter) = Unit

        override fun onDeletePlant(plant: Plant) = Unit

        override fun onUndoDelete() = Unit

        override fun onPlantClick(plant: Plant) = Unit

        override fun onAddPlantClick() = Unit

    }

}