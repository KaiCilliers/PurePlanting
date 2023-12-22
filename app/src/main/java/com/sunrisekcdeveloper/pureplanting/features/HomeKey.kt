package com.sunrisekcdeveloper.pureplanting.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.design.ui.SnackbarEmitter
import com.sunrisekcdeveloper.design.ui.SnackbarEmitterType
import com.sunrisekcdeveloper.home.HomeUi
import com.sunrisekcdeveloper.home.HomeViewModel
import com.sunrisekcdeveloper.home.models.PlantTabFilter
import com.sunrisekcdeveloper.home.subcomponents.NotificationIconViewModel
import com.sunrisekcdeveloper.home.subcomponents.PlantListViewModel
import com.sunrisekcdeveloper.navigation.ComposeKey
import com.sunrisekcdeveloper.notification.domain.NotificationRepository
import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import com.sunrisekcdeveloper.pureplanting.navigation.NavigationServiceProvider
import com.zhuinden.simplestack.ScopeKey
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackcomposeintegration.services.rememberService
import com.zhuinden.simplestackextensions.fragmentsktx.lookup
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import com.zhuinden.simplestackextensions.servicesktx.rebind
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class HomeKey(
    val overrideSelectedFilter: PlantTabFilter? = null
) : ComposeKey(), ScopeKey.Child {

    @Composable
    override fun ScreenComposable(modifier: Modifier) {
        val emitter = rememberService<SnackbarEmitter>()
        val viewModel = rememberService<HomeViewModel>()
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            scope.launch {
                while (true) {
                    emitter.emit(SnackbarEmitterType.Text("Nice ${(0..100).random()}"))
                    delay(5000L)
                }
            }
        }
        HomeUi(viewModel)
    }

    override fun getParentScopes() = listOf(NavigationServiceProvider.Scopes.NOTIFICATION)

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
}