package com.sunrisekcdeveloper.pureplanting.features.detail

import com.sunrisekcdeveloper.pureplanting.domain.plant.Plant

interface Router {
    fun goToEditPlant(plant: Plant)
    fun goBack()
}