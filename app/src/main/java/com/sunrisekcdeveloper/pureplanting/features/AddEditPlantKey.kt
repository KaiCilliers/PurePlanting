package com.sunrisekcdeveloper.pureplanting.features

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.addEditPlant.AddEditPlantViewModel
import com.sunrisekcdeveloper.addEditPlant.Router
import com.sunrisekcdeveloper.android.ParcelablePlant
import com.sunrisekcdeveloper.navigation.FragmentKey
import com.sunrisekcdeveloper.plant.PlantCache
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddEditPlantKey(
    val plant: ParcelablePlant? = null
) : FragmentKey() {
    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            val router = object : Router {
                override fun jumpToRoot() {
                    backstack.jumpToRoot()
                }

            }
            add(AddEditPlantViewModel(lookup<PlantCache>(), plant, router))
        }
    }

    override fun instantiateFragment(): Fragment = AddEditPlantFragment()
}