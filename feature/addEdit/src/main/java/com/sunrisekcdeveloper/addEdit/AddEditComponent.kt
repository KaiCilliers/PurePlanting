package com.sunrisekcdeveloper.addEdit

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.DayOfWeek
import java.time.LocalTime

interface AddEditComponent {

    val image: StateFlow<String>

    val name: StateFlow<String>

    val description: StateFlow<String>

    val size: StateFlow<PlantSize>

    val wateringDays: StateFlow<List<DayOfWeek>>

    val wateringTime: StateFlow<LocalTime>

    val wateringAmount: StateFlow<String>

    fun onSavePlant()

    fun onImageChanged(image: String)

    fun onNameChanged(name: String)

    fun onDescriptionChanged(description: String)

    fun onSizeChanged(size: PlantSize)

    fun onWateringDaysChanged(wateringDays: List<DayOfWeek>)

    fun onWateringTimeChanged(wateringTime: LocalTime)

    fun onWateringAmountChanged(wateringAmount: String)

    class Fake: AddEditComponent {
        override val image: StateFlow<String> = MutableStateFlow("image src uri")
        override val name: StateFlow<String> = MutableStateFlow("name")
        override val description: StateFlow<String> = MutableStateFlow("description")
        override val size: StateFlow<PlantSize> = MutableStateFlow(PlantSize.Large)
        override val wateringDays: StateFlow<List<DayOfWeek>> = MutableStateFlow(listOf(DayOfWeek.MONDAY, DayOfWeek.SATURDAY))
        override val wateringTime: StateFlow<LocalTime> = MutableStateFlow(LocalTime.now())
        override val wateringAmount: StateFlow<String> = MutableStateFlow("120ml")

        override fun onSavePlant() = Unit
        override fun onImageChanged(image: String) = Unit
        override fun onNameChanged(name: String) = Unit
        override fun onDescriptionChanged(description: String) = Unit
        override fun onSizeChanged(size: PlantSize) = Unit
        override fun onWateringDaysChanged(wateringDays: List<DayOfWeek>) = Unit
        override fun onWateringTimeChanged(wateringTime: LocalTime) = Unit
        override fun onWateringAmountChanged(wateringAmount: String) = Unit

    }
}