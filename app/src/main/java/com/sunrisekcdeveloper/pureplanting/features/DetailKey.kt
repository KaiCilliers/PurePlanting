package com.sunrisekcdeveloper.pureplanting.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.sunrisekcdeveloper.detail.DetailUi
import com.sunrisekcdeveloper.detail.DetailViewModel
import com.sunrisekcdeveloper.detail.Router
import com.sunrisekcdeveloper.navigation.ComposeKey
import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackcomposeintegration.services.rememberService
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import com.zhuinden.simplestackextensions.servicesktx.rebind
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class DetailKey(
    val plant: Plant
) : ComposeKey() {

    @Composable
    override fun ScreenComposable(modifier: Modifier) {
        val viewModel = rememberService<DetailViewModel>()

        DetailUi(viewModel)
    }

    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            DetailViewModel.Default(
                plant,
                lookup<PlantRepository>(),
                router = object : Router {
                    override fun goToEditPlant(plant: Plant) {
                        backstack.goTo(AddEditKey(plant))
                    }

                    override fun goBack() {
                        backstack.goBack()
                    }
                }
            ).let { viewModel ->
                add(viewModel)
                rebind<DetailViewModel>(viewModel)
            }
        }
    }
}