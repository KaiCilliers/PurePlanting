package com.sunrisekcdeveloper.detail

import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import com.zhuinden.simplestack.Bundleable
import com.zhuinden.statebundle.StateBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime

interface DetailViewModel {

    val plant: StateFlow<Plant>

    fun onWaterPlant()
    fun onEditPlant()
    fun onGoBack()

    class Fake : DetailViewModel {
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

        override fun onGoBack() = Unit
    }

    class Default(
        plant: Plant,
        private val plantRepository: PlantRepository,
        private val router: Router
    ): DetailViewModel, Bundleable {

        private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

        override val plant = MutableStateFlow(plant)

        override fun onWaterPlant() {
            viewModelScope.launch {
                router.goBack()
                val watered = plant.value.water()
                plantRepository.save(watered)
                plant.update { watered }
            }
        }

        override fun onEditPlant() {
            router.goToEditPlant(plant.value)
        }

        override fun onGoBack() {
            router.goBack()
        }

        override fun toBundle(): StateBundle = StateBundle().apply {
            putParcelable("plant", plant.value)
        }

        override fun fromBundle(bundle: StateBundle?) {
            bundle?.run {
                getParcelable<Plant>("plant")?.let { savedPlant ->
                    plant.update { savedPlant }
                }
            }
        }
    }
}

