package com.sunrisekcdeveloper.pureplanting.features

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.android.ParcelablePlant
import com.sunrisekcdeveloper.navigation.FragmentKey
import com.sunrisekcdeveloper.notification.NotificationCache
import com.sunrisekcdeveloper.plant.PlantCache
import com.sunrisekcdeveloper.plantList.PlantsRouter
import com.sunrisekcdeveloper.plantList.PlantsViewModel
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import kotlinx.parcelize.Parcelize

@Parcelize
data object PlantsKey : FragmentKey() {
    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            val router = object : PlantsRouter {
                override fun goToNotificationList() {
                    backstack.goTo(NotificationsKey)
                }

                override fun goToAddPlant() {
                    backstack.goTo(AddEditPlantKey())
                }

                override fun goToPlantDetail(plant: ParcelablePlant) {
                    backstack.goTo(PlantDetailKey(plant))
                }

            }
            add(PlantsViewModel(lookup<PlantCache>(), lookup<NotificationCache>(), router))
        }
    }

    override fun instantiateFragment(): Fragment = PlantsFragment()
}