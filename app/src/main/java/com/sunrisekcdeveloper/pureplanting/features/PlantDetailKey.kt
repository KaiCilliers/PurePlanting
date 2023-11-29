package com.sunrisekcdeveloper.pureplanting.features

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.android.ParcelablePlant
import com.sunrisekcdeveloper.navigation.FragmentKey
import com.sunrisekcdeveloper.plant.PlantCache
import com.sunrisekcdeveloper.plantDetail.PlantDetailsViewModel
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlantDetailKey(
    val plant: ParcelablePlant
) : FragmentKey() {
    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            add(PlantDetailsViewModel(plant, lookup<PlantCache>()))
        }
    }

    override fun instantiateFragment(): Fragment = PlantDetailFragment()
}