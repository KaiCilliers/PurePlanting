package com.sunrisekcdeveloper.detail

import com.sunrisekcdeveloper.plant.Plant

interface Router {
    fun goToEditPlant(plant: Plant)
    fun goBack()
}