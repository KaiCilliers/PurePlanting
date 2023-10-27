package com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant

import com.sunrisekcdeveloper.pureplanting.features.presentation.plants.Plant
import com.sunrisekcdeveloper.pureplanting.features.component.PlantCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditPlantViewModel(
    private val plantCache: PlantCache,
    private val plant: Plant?,
) {

    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

    var image = plant?.details?.imageSrcUri ?: ""
    var name = plant?.details?.name ?: ""
    var description = plant?.details?.description ?: ""
    var size = plant?.details?.size ?: ""
    var wateringDays = plant?.wateringInfo?.days ?: emptyList()
    var wateringHour = plant?.wateringInfo?.atHour ?: DEFAULT_WATERING_HOUR
    var wateringAmount = plant?.wateringInfo?.amount ?: DEFAULT_WATERING_AMOUNT

    fun savePlant() {
        if (plant == null) {
            createPlant()
        } else {
            updatePlant()
        }
    }

    private fun createPlant() = viewModelScope.launch {
        val newPlant = Plant.createNewPlant(
            imageSrc = image,
            name = name,
            description = description,
            size = size,
            wateringDays = wateringDays,
            wateringHour = wateringHour,
            wateringAmount = wateringAmount // TODO: make use of typealias to define unique types OR use value classes
        )
        // validation can be placed here or enforced in the UI OR both
        plantCache.save(newPlant)
        // notification can be sent after sending
        // navigation can be performed now
    }

    private fun updatePlant() = viewModelScope.launch {
        val updatedPlant = plant!!.copy(
            id = plant.id,
            details = plant.details.copy(
                name = name,
                size = size,
                description = description,
                imageSrcUri = image,
            ),
            wateringInfo = plant.wateringInfo.copy(
                atHour = wateringHour,
                days = wateringDays,
                amount = wateringAmount,
            )
        )
        plantCache.save(updatedPlant)
        // notification can be sent after sending
        // navigation can be performed now
    }

    companion object {
        const val DEFAULT_WATERING_HOUR = 8
        const val DEFAULT_WATERING_AMOUNT = "250ml"
    }
}