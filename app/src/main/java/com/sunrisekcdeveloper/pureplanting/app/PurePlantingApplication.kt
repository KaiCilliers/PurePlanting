package com.sunrisekcdeveloper.pureplanting.app

import android.app.Application
import com.sunrisekcdeveloper.pureplanting.features.component.plants.InMemoryPlantCache
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantCache
import com.zhuinden.simplestack.GlobalServices
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.rebind

class PurePlantingApplication : Application() {
    lateinit var globalServices: GlobalServices
        private set

    override fun onCreate() {
        super.onCreate()

        // Create global dependencies
        val inMemoryPlantCache = InMemoryPlantCache()

     globalServices = GlobalServices.builder()
         // add dependencies here
         .add(inMemoryPlantCache)
         .rebind<PlantCache>(inMemoryPlantCache)
         .build()
    }
}