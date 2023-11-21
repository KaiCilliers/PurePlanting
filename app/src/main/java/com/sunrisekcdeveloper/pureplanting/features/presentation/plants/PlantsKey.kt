package com.sunrisekcdeveloper.pureplanting.features.presentation.plants

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationsCache
import com.sunrisekcdeveloper.pureplanting.features.component.plants.PlantCache
import com.sunrisekcdeveloper.pureplanting.navigation.FragmentKey
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import kotlinx.parcelize.Parcelize

@Parcelize
data object PlantsKey : FragmentKey() {
    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            add(PlantsViewModel(lookup<PlantCache>(), lookup<NotificationsCache>(), backstack = backstack))
        }
    }

    override fun instantiateFragment(): Fragment = PlantsFragment()
}