package com.sunrisekcdeveloper.pureplanting.features

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.navigation.FragmentKey
import com.sunrisekcdeveloper.notification.domain.NotificationRepository
import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import com.sunrisekcdeveloper.home.HomeViewModel
import com.sunrisekcdeveloper.home.subcomponents.NotificationIconViewModel
import com.sunrisekcdeveloper.home.subcomponents.PlantListViewModel
import com.sunrisekcdeveloper.home.models.PlantTabFilter
import com.sunrisekcdeveloper.pureplanting.navigation.NavigationServiceProvider
import com.zhuinden.simplestack.ScopeKey
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import com.zhuinden.simplestackextensions.servicesktx.rebind
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeKey(
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
            val notificationIconRouter = NotificationIconViewModel.Router { backstack.goTo(NotificationListKey) }
            val homeViewModel = HomeViewModel.Default(
                plantRepository = lookup<PlantRepository>(),
                notificationRepository = lookup<NotificationRepository>(),
                plantListRouter = plantListRouter,
                notificationIconRouter = notificationIconRouter,
                plantListComp = lookup<PlantListViewModel>()
            )
            add(homeViewModel)
            rebind<HomeViewModel>(homeViewModel)
        }
    }

    override fun instantiateFragment(): Fragment = HomeFragment()

    override fun getParentScopes() = listOf(NavigationServiceProvider.Scopes.NOTIFICATION)
}