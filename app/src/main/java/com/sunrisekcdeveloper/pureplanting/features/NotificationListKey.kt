package com.sunrisekcdeveloper.pureplanting.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.sunrisekcdeveloper.home.models.PlantTabFilter
import com.sunrisekcdeveloper.home.subcomponents.PlantListViewModel
import com.sunrisekcdeveloper.navigation.ComposeKey
import com.sunrisekcdeveloper.notification.domain.NotificationRepository
import com.sunrisekcdeveloper.notification.domain.PlantNotificationType
import com.sunrisekcdeveloper.notificationList.NotificationListUi
import com.sunrisekcdeveloper.notificationList.NotificationListViewModel
import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import com.sunrisekcdeveloper.pureplanting.navigation.NavigationServiceProvider
import com.zhuinden.simplestack.ScopeKey
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestack.StateChange
import com.zhuinden.simplestackcomposeintegration.services.rememberService
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import com.zhuinden.simplestackextensions.servicesktx.rebind
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data object NotificationListKey : ComposeKey(), ScopeKey.Child {

    @Composable
    override fun ScreenComposable(modifier: Modifier) {
        val viewModel = rememberService<NotificationListViewModel>()
        NotificationListUi(viewModel)
    }

    override fun getParentScopes() = listOf(NavigationServiceProvider.Scopes.HOME)

    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            val plantListViewModel = lookup<PlantListViewModel>()
            val notificationIconViewModel = NotificationListViewModel.Default(
                notificationRepository = lookup<NotificationRepository>(),
                plantRepository = lookup<PlantRepository>(),
                router = object : NotificationListViewModel.Router {
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
                        backstack.jumpToRoot(StateChange.BACKWARD)
                    }

                    override fun goToDetail(plant: Plant) {
                        backstack.goTo(DetailKey(plant))
                    }

                }
            )
            add(notificationIconViewModel)
            rebind<NotificationListViewModel>(notificationIconViewModel)
        }
    }
}