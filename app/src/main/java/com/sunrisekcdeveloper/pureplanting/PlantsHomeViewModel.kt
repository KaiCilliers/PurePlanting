package com.sunrisekcdeveloper.pureplanting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PlantsHomeViewModel(
): ViewModel() {
    val plants: StateFlow<List<Plant>>
        get() = _plants
    private val _plants = MutableStateFlow<List<Plant>>(emptyList())

    fun addPlant(plant: Plant) {
        _plants.update {
            val mutableList = it.toMutableList()
            mutableList.add(plant)
            mutableList
        }
    }
}