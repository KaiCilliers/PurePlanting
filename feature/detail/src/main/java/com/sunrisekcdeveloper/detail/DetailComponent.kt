package com.sunrisekcdeveloper.detail

import com.sunrisekcdeveloper.plant.domain.Plant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.DayOfWeek
import java.time.LocalTime

interface DetailComponent {

    val plant: StateFlow<Plant>

    fun onWaterPlant()
    fun onEditPlant()

    class Fake : DetailComponent {
        override val plant: StateFlow<Plant> = MutableStateFlow(
            Plant.createNewPlant(
                name = "Plant Name",
                size = "Medium",
                description = "Plant Description",
                imageSrc = "",
                wateringDays = listOf(DayOfWeek.SATURDAY),
                wateringTime = LocalTime.now(),
                wateringAmount = "240ml"
            )
        )

        override fun onWaterPlant() = Unit

        override fun onEditPlant() = Unit
    }
}

