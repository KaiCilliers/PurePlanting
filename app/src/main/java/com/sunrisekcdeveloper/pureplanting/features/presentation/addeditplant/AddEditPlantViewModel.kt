package com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant

import com.sunrisekcdeveloper.pureplanting.features.component.plants.Plant
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantCache
import com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant.components.PlantSize
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestack.Bundleable
import com.zhuinden.statebundle.StateBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

class AddEditPlantViewModel(
    private val plantCache: PlantCache,
    private val plant: Plant?,
    private val backstack: Backstack,
) : Bundleable {

    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

    val image = MutableStateFlow(plant?.details?.imageSrcUri ?: "")
    val name = MutableStateFlow(plant?.details?.name ?: "")
    val description = MutableStateFlow(plant?.details?.description ?: "")
    val size = MutableStateFlow(PlantSize.valueOf(plant?.details?.size ?: DEFAULT_PLANT_SIZE.name))
    val wateringDays = MutableStateFlow(plant?.wateringInfo?.days ?: listOf(DEFAULT_WATERING_DAY))
    val wateringTime = MutableStateFlow(plant?.wateringInfo?.time ?: DEFAULT_WATERING_TIME)
    val wateringAmount = MutableStateFlow(plant?.wateringInfo?.amount ?: DEFAULT_WATERING_AMOUNT)

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
            size = size.value.name,
            wateringDays = wateringDays.value,
            wateringTime = wateringTime.value,
            wateringAmount = wateringAmount.value // TODO: make use of typealias to define unique types OR use value classes
        )
        // validation can be placed here or enforced in the UI OR both
        plantCache.save(newPlant)
        // notification can be sent after sending
        backstack.jumpToRoot()
    }

    private fun updatePlant() = viewModelScope.launch {
        val updatedPlant = plant!!.copy(
            id = plant.id,
            details = plant.details.copy(
                name = name.value,
                size = size.value.name,
                description = description.value,
                imageSrcUri = image.value,
            ),
            wateringInfo = plant.wateringInfo.copy(
                days = wateringDays.value,
                amount = wateringAmount.value,
                time = wateringTime.value,
                datesWatered = plant.wateringInfo.datesWatered
            ),
            userLastModifiedDate = LocalDateTime.now()
        )
        plantCache.save(updatedPlant)
        // notification can be sent after sending
        backstack.jumpToRoot()
    }

    companion object {
        val DEFAULT_WATERING_TIME: LocalTime = LocalTime.of(8,0)
        const val DEFAULT_WATERING_AMOUNT = "250"
        val DEFAULT_PLANT_SIZE = PlantSize.Medium
        val DEFAULT_WATERING_DAY = DayOfWeek.MONDAY
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