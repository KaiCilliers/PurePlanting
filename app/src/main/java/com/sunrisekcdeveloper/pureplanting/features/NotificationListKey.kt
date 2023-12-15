package com.sunrisekcdeveloper.pureplanting.features

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.navigation.FragmentKey
import com.sunrisekcdeveloper.notification.domain.NotificationRepository
import com.sunrisekcdeveloper.notification.domain.PlantNotificationType
import com.sunrisekcdeveloper.notificationList.NotificationListComponent
import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.plant.domain.PlantRepository
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
data object NotificationListKey : FragmentKey(), ScopeKey.Child {
    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            val plantListViewModel = lookup<PlantListViewModel>()
            val component = NotificationListComponent.Default(
                notificationRepository = lookup<NotificationRepository>(),
                plantRepository = lookup<PlantRepository>(),
                router = object : NotificationListComponent.Router {
                    override fun goBack() {
                        backstack.goBack()
                    }

                    override fun goToMain(notificationType: PlantNotificationType) {
                        plantListViewModel.onFilterChange(
                            when (notificationType) {
                                is PlantNotificationType.NeedsWater -> PlantTabFilter.UPCOMING
                                is PlantNotificationType.ForgotToWater -> PlantTabFilter.FORGOT_TO_WATER
                            }
                        )
                        backstack.goTo(MainKey())
                    }

                    override fun goToDetail(plant: Plant) {
                        backstack.goTo(DetailKey(plant))
                    }

                }
            )
            add(component)
            rebind<NotificationListComponent>(component)
        }
    }

    override fun instantiateFragment(): Fragment = NotificationsFragment()

    override fun getParentScopes() = listOf(NavigationServiceProvider.Scopes.NOTIFICATION)
}