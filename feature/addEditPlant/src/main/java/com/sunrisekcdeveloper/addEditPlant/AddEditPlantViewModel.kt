package com.sunrisekcdeveloper.addEditPlant

import com.sunrisekcdeveloper.android.ParcelablePlant
import com.sunrisekcdeveloper.android.toDomain
import com.sunrisekcdeveloper.plant.Plant
import com.sunrisekcdeveloper.plant.PlantCache
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
    parcelablePlant: ParcelablePlant?,
    private val router: Router,
) : Bundleable {

    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
    private val plant = parcelablePlant?.toDomain()

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
            updatePlant(plant)
        }
    }

    private fun createPlant() = viewModelScope.launch {
        val newPlant = com.sunrisekcdeveloper.plant.Plant.createNewPlant(
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
        router.jumpToRoot()
    }

    private fun updatePlant(plantToUpdate: Plant) = viewModelScope.launch {
        val updatedPlant = plantToUpdate.copy(
            id = plantToUpdate.id,
            details = plantToUpdate.details.copy(
                name = name.value,
                size = size.value.name,
                description = description.value,
                imageSrcUri = image.value,
            ),
            wateringInfo = plantToUpdate.wateringInfo.copy(
                days = wateringDays.value,
                amount = wateringAmount.value,
                time = wateringTime.value,
                datesWatered = plantToUpdate.wateringInfo.datesWatered
            ),
            userLastModifiedDate = LocalDateTime.now()
        )
        plantCache.save(updatedPlant)
        // notification can be sent after sending
        router.jumpToRoot()
    }

    companion object {
        val DEFAULT_WATERING_TIME: LocalTime = LocalTime.of(8, 0)
        const val DEFAULT_WATERING_AMOUNT = "250"
        val DEFAULT_PLANT_SIZE = PlantSize.Medium
        val DEFAULT_WATERING_DAY = DayOfWeek.MONDAY
    }

    override fun toBundle(): StateBundle = StateBundle().apply {
        putString("name", name.value)
        putString("description", description.value)
        putString("size", size.value.toString())
        putString("wateringDays", wateringDays.value.joinToString())
        putString("wateringTime", wateringTime.toString())
        putString("wateringAmount", wateringAmount.toString())
        putString("imgSrcUri", image.value)
    }

    override fun fromBundle(bundle: StateBundle?) {
        bundle?.run {
            name.update { getString("name", "") }
            description.update { getString("description", "") }
            size.update { PlantSize.valueOf(getString("size", DEFAULT_PLANT_SIZE.toString())) }
            wateringAmount.update { getString("wateringAmount", DEFAULT_WATERING_AMOUNT) }
            image.update { getString("imgSrcUri", "") }
            getString("wateringDays")?.let { savedDays ->
                savedDays
                    .split(",")
                    .map { DayOfWeek.valueOf(it) }
                    .also { savedDaysOfWeek -> wateringDays.update { savedDaysOfWeek } }
            }
            getString("wateringTime")?.let { savedTime ->
                wateringTime.update { LocalTime.parse(savedTime) }
            }
        }
    }
}