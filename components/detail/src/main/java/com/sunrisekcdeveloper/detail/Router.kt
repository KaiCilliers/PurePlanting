package com.sunrisekcdeveloper.detail

import com.sunrisekcdeveloper.plant.domain.Plant

interface Router {
    fun goToEditPlant(plant: Plant)
    fun goBack()
}