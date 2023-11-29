package com.sunrisekcdeveloper.plantList

import com.sunrisekcdeveloper.android.ParcelablePlant
import com.sunrisekcdeveloper.plant.Plant

interface PlantsRouter {
    fun goToNotificationList()
    fun goToAddPlant()
    fun goToPlantDetail(plant: ParcelablePlant)
}