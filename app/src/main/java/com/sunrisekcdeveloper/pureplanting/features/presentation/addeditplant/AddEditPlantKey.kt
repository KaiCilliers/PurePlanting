package com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.pureplanting.features.component.plants.Plant
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantCache
import com.sunrisekcdeveloper.pureplanting.navigation.FragmentKey
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddEditPlantKey(
    val plant: Plant? = null
) : FragmentKey() {
    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            val key = getKey<AddEditPlantKey>()

            add(AddEditPlantViewModel(lookup<PlantCache>(), key.plant))
        }
    }

    override fun instantiateFragment(): Fragment = AddEditPlantFragment()
}