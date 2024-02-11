package com.sunrisekcdeveloper.pureplanting.features.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.sunrisekcdeveloper.pureplanting.core.design.ui.SnackbarEmitter
import com.sunrisekcdeveloper.pureplanting.core.navigation.ComposeKey
import com.sunrisekcdeveloper.pureplanting.core.navigation.NavigationServiceProvider
import com.sunrisekcdeveloper.pureplanting.domain.notification.NotificationRepository
import com.sunrisekcdeveloper.pureplanting.domain.plant.Plant
import com.sunrisekcdeveloper.pureplanting.domain.plant.PlantRepository
import com.sunrisekcdeveloper.pureplanting.features.addEdit.AddEditKey
import com.sunrisekcdeveloper.pureplanting.features.detail.DetailKey
import com.sunrisekcdeveloper.pureplanting.features.home.models.PlantTabFilter
import com.sunrisekcdeveloper.pureplanting.features.notificationList.NotificationListKey
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
    }
}