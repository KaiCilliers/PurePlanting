package com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant

import com.sunrisekcdeveloper.pureplanting.features.component.plants.Plant
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantCache
import com.zhuinden.simplestack.Bundleable
import com.zhuinden.statebundle.StateBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek

class AddEditPlantViewModel(
    private val plantCache: PlantCache,
    private val plant: Plant?,
) : Bundleable {

    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

    val image = MutableStateFlow( plant?.details?.imageSrcUri ?: "")
    val name = MutableStateFlow(plant?.details?.name ?: "")
    val description = MutableStateFlow( plant?.details?.description ?: "")
    val size = MutableStateFlow(plant?.details?.size ?: "")
    val wateringDays = MutableStateFlow( plant?.wateringInfo?.days ?: listOf(DayOfWeek.MONDAY))
    val wateringHour = MutableStateFlow(plant?.wateringInfo?.atHour ?: DEFAULT_WATERING_HOUR)
    val wateringAmount = MutableStateFlow( plant?.wateringInfo?.amount ?: DEFAULT_WATERING_AMOUNT)

    fun savePlant() {
        if (plant == null) {
            createPlant()
        } else {
            updatePlant()
        }
    }

    private fun createPlant() = viewModelScope.launch {
        val newPlant = Plant.createNewPlant(
            imageSrc = image.value,
            name = name.value,
            description = description.value,
            size = size.value,
            wateringDays = wateringDays.value,
            wateringHour = wateringHour.value,
            wateringAmount = wateringAmount.value // TODO: make use of typealias to define unique types OR use value classes
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
                name = name.value,
                size = size.value,
                description = description.value,
                imageSrcUri = image.value,
            ),
            wateringInfo = plant.wateringInfo.copy(
                atHour = wateringHour.value,
                days = wateringDays.value,
                amount = wateringAmount.value,
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

    override fun toBundle(): StateBundle = StateBundle().apply {
        putString("name", name.value)
        putString("description", description.value)
        // todo add other properties
    }

    override fun fromBundle(bundle: StateBundle?) {
        bundle?.run {
            name.update { getString("name", "") }
            description.update { getString("description", "") }
        }
    }
}