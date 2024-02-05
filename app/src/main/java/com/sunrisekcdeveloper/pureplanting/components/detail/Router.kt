package com.sunrisekcdeveloper.pureplanting.components.detail

import com.sunrisekcdeveloper.pureplanting.business.plant.Plant

interface Router {
    fun goToEditPlant(plant: Plant)
    fun goBack()
}