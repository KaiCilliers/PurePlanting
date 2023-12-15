package com.sunrisekcdeveloper.pureplanting.features

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.navigation.FragmentKey
import com.sunrisekcdeveloper.notification.domain.NotificationRepository
import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import com.sunrisekcdeveloper.plantList.MainComponent
import com.sunrisekcdeveloper.plantList.NotificationIconComponent
import com.sunrisekcdeveloper.plantList.PlantListViewModel
import com.sunrisekcdeveloper.plantList.PlantTabFilter
import com.sunrisekcdeveloper.pureplanting.navigation.NavigationServiceProvider
import com.zhuinden.simplestack.ScopeKey
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import com.zhuinden.simplestackextensions.servicesktx.rebind
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainKey(
    val overrideSelectedFilter: PlantTabFilter? = null
) : FragmentKey(), ScopeKey.Child {
    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            val plantListRouter = object : PlantListViewModel.Router {
                override fun goToAddPlant() {
                    backstack.goTo(AddEditKey())
                }

                override fun goToPlantDetail(plant: Plant) {
                    backstack.goTo(DetailKey(plant))
                }

            }
            val notificationIconRouter = NotificationIconComponent.Router { backstack.goTo(NotificationListKey) }
            val mainComponent = MainComponent.Default(
                plantRepository = lookup<PlantRepository>(),
                notificationRepository = lookup<NotificationRepository>(),
                plantListViewModelRouter = plantListRouter,
                notificationIconComponentRouter = notificationIconRouter,
                plantListComp = lookup<PlantListViewModel>()
            )
            add(mainComponent)
            rebind<MainComponent>(mainComponent)
        }
    }

    override fun instantiateFragment(): Fragment = MainFragment()

    override fun getParentScopes() = listOf(NavigationServiceProvider.Scopes.NOTIFICATION)
}