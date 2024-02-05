package com.sunrisekcdeveloper.pureplanting.components.addEdit

import com.sunrisekcdeveloper.pureplanting.components.addEdit.models.PlantSize
import com.sunrisekcdeveloper.design.ui.SnackbarEmitter
import com.sunrisekcdeveloper.design.ui.SnackbarEmitterType
import com.sunrisekcdeveloper.plant.Plant
import com.sunrisekcdeveloper.plant.PlantRepository
import com.zhuinden.simplestack.Bundleable
import com.zhuinden.statebundle.StateBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

interface AddEditViewModel {

    interface Router {
        fun jumpToRoot()
        fun goBack()
    }

    val image: StateFlow<String>

    val name: StateFlow<String>

    val description: StateFlow<String>

    val size: StateFlow<PlantSize>

    val wateringDays: StateFlow<List<DayOfWeek>>

    val wateringTime: StateFlow<LocalTime>

    val wateringAmount: StateFlow<String>

    fun onSavePlant()

    fun onBackClick()

    fun onImageChanged(image: String)

    fun onNameChanged(name: String)

    fun onDescriptionChanged(description: String)

    fun onSizeChanged(size: PlantSize)

    fun onWateringDaysChanged(wateringDays: List<DayOfWeek>)

    fun onWateringTimeChanged(wateringTime: LocalTime)

    fun onWateringAmountChanged(wateringAmount: String)

    class Fake: AddEditViewModel {
        override val image: StateFlow<String> = MutableStateFlow("image src uri")
        override val name: StateFlow<String> = MutableStateFlow("name")
        override val description: StateFlow<String> = MutableStateFlow("description")
        override val size: StateFlow<PlantSize> = MutableStateFlow(PlantSize.Large)
        override val wateringDays: StateFlow<List<DayOfWeek>> = MutableStateFlow(listOf(DayOfWeek.MONDAY, DayOfWeek.SATURDAY))
        override val wateringTime: StateFlow<LocalTime> = MutableStateFlow(LocalTime.now())
        override val wateringAmount: StateFlow<String> = MutableStateFlow("120ml")

        override fun onSavePlant() = Unit
        override fun onBackClick() = Unit

        override fun onImageChanged(image: String) = Unit
        override fun onNameChanged(name: String) = Unit
        override fun onDescriptionChanged(description: String) = Unit
        override fun onSizeChanged(size: PlantSize) = Unit
        override fun onWateringDaysChanged(wateringDays: List<DayOfWeek>) = Unit
        override fun onWateringTimeChanged(wateringTime: LocalTime) = Unit
        override fun onWateringAmountChanged(wateringAmount: String) = Unit

    }

    class Default(
        private val plantRepository: PlantRepository,
        private val router: Router,
        private val eventEmitter: SnackbarEmitter,
        private val plant: Plant?,
    ) : AddEditViewModel, Bundleable {

        private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
        private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        override val image = MutableStateFlow(plant?.details?.imageSrcUri ?: "")
        override val name = MutableStateFlow(plant?.details?.name ?: "")
        override val description = MutableStateFlow(plant?.details?.description ?: "")
        override val size = MutableStateFlow(PlantSize.valueOf(plant?.details?.size ?: DEFAULT_PLANT_SIZE.name))
        override val wateringDays = MutableStateFlow(plant?.wateringInfo?.days ?: listOf(DEFAULT_WATERING_DAY))
        override val wateringTime = MutableStateFlow(plant?.wateringInfo?.time ?: DEFAULT_WATERING_TIME)
        override val wateringAmount = MutableStateFlow(plant?.wateringInfo?.amount ?: DEFAULT_WATERING_AMOUNT)

        private fun createPlant() = viewModelScope.launch {
            val newPlant = Plant.createNewPlant(
                imageSrc = image.value,
                name = name.value,
                description = description.value,
                size = size.value.name,
                wateringDays = wateringDays.value,
                wateringTime = wateringTime.value,
                wateringAmount = wateringAmount.value
            )
            // validation can be placed here or enforced in the UI OR both
            plantRepository.save(newPlant)
            eventEmitter.emit(SnackbarEmitterType.Text("Created new plant \"${newPlant.details.name}\"!"))
            router.jumpToRoot()
        }

        private fun updatePlant(plantToUpdate: Plant) = viewModelScope.launch {
            val waterDaysUpdated = plantToUpdate.wateringInfo.days != wateringDays.value
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
                    history = plantToUpdate.wateringInfo.history,
                    lastModifiedWateringDays = if (waterDaysUpdated) LocalDateTime.now() else plantToUpdate.wateringInfo.lastModifiedWateringDays
                )
            )
            plantRepository.save(updatedPlant)
            eventEmitter.emit(SnackbarEmitterType.Text("Updated \"${updatedPlant.details.name}\"!"))
            router.jumpToRoot()
        }

        override fun onSavePlant() {
            if (plant == null) {
                createPlant()
            } else {
                updatePlant(plant)
            }
        }

        override fun onBackClick() {
            router.goBack()
        }

        override fun onImageChanged(image: String) {
            this.image.value = image
        }

        override fun onNameChanged(name: String) {
            this.name.value = name
        }

        override fun onDescriptionChanged(description: String) {
            this.description.value = description
        }

        override fun onSizeChanged(size: PlantSize) {
            this.size.value = size
        }

        override fun onWateringDaysChanged(wateringDays: List<DayOfWeek>) {
            this.wateringDays.value = wateringDays
        }

        override fun onWateringTimeChanged(wateringTime: LocalTime) {
            this.wateringTime.value = wateringTime
        }

        override fun onWateringAmountChanged(wateringAmount: String) {
            this.wateringAmount.value = wateringAmount
        }

        override fun toBundle(): StateBundle = StateBundle().apply {
            putString("name", name.value)
            putString("description", description.value)
            putString("size", size.value.toString())
            putString("wateringDays", wateringDays.value.joinToString(","))
            putString("wateringTime", wateringTime.value.format(formatter))
            putString("wateringAmount", wateringAmount.value)
            if (image.value.isNotEmpty()) putString("imgSrcUri", image.value)
        }

        override fun fromBundle(bundle: StateBundle?) {
            bundle?.run {
                name.value = getString("name", "")
                description.value = getString("description", "")
                size.value = PlantSize.valueOf(getString("size", DEFAULT_PLANT_SIZE.toString()))
                wateringAmount.value = getString("wateringAmount", DEFAULT_WATERING_AMOUNT)
                val imageSrc = getString("imgSrcUri", "")
                if (imageSrc.isNotEmpty()) image.value = imageSrc
                getString("wateringDays")?.let { savedDays ->
                    savedDays
                        .split(",")
                        .map { DayOfWeek.valueOf(it) }
                        .also { savedDaysOfWeek -> wateringDays.value = savedDaysOfWeek }
                }
                getString("wateringTime")?.let { savedTime ->
                    wateringTime.value = LocalTime.parse(savedTime, formatter)
                }
            }
        }

        companion object {
            val DEFAULT_WATERING_TIME: LocalTime = LocalTime.of(8, 0)
            const val DEFAULT_WATERING_AMOUNT = "250"
            val DEFAULT_PLANT_SIZE = PlantSize.Medium
            val DEFAULT_WATERING_DAY = DayOfWeek.MONDAY
        }
    }
}