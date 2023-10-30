package com.sunrisekcdeveloper.pureplanting.features.presentation.plantdetail

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.pureplanting.features.component.plants.InMemoryPlantCache
import com.sunrisekcdeveloper.pureplanting.features.component.plants.Plant
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantCache
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantDetails
import com.sunrisekcdeveloper.pureplanting.navigation.FragmentKey
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlantDetailKey(
    val plant: Plant
) : FragmentKey() {
    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            val key = getKey<PlantDetailKey>()
            add(PlantDetailsViewModel(key.plant, lookup<PlantCache>()))
        }
    }

    override fun instantiateFragment(): Fragment = PlantDetailFragment()
}