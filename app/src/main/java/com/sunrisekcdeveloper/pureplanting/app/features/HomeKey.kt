package com.sunrisekcdeveloper.pureplanting.app.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.sunrisekcdeveloper.design.ui.SnackbarEmitter
import com.sunrisekcdeveloper.notification.NotificationRepository
import com.sunrisekcdeveloper.plant.Plant
import com.sunrisekcdeveloper.plant.PlantRepository
import com.sunrisekcdeveloper.pureplanting.app.navigation.ComposeKey
import com.sunrisekcdeveloper.pureplanting.app.navigation.NavigationServiceProvider
import com.sunrisekcdeveloper.pureplanting.components.home.HomeUi
import com.sunrisekcdeveloper.pureplanting.components.home.HomeViewModel
import com.sunrisekcdeveloper.pureplanting.components.home.models.PlantTabFilter
import com.sunrisekcdeveloper.pureplanting.components.home.subcomponents.NotificationIconViewModel
import com.sunrisekcdeveloper.pureplanting.components.home.subcomponents.PlantListViewModel
import com.zhuinden.simplestack.ScopeKey
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackcomposeintegration.services.rememberService
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import com.zhuinden.simplestackextensions.servicesktx.rebind
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class HomeKey(
    val overrideSelectedFilter: PlantTabFilter? = null
) : ComposeKey(), ScopeKey.Child {

    @Composable
    override fun ScreenComposable(modifier: Modifier) {
        val viewModel = rememberService<HomeViewModel>()
        HomeUi(viewModel)
    }

    override fun getParentScopes() = listOf(NavigationServiceProvider.Scopes.HOME)

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
                eventEmitter = lookup<SnackbarEmitter>(),
                notificationIconRouter = notificationIconRouter,
                plantListComp = lookup<PlantListViewModel>()
            )
            add(homeViewModel)
            rebind<HomeViewModel>(homeViewModel)
        }
    }
}