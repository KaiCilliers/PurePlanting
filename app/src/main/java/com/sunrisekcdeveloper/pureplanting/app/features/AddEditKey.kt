package com.sunrisekcdeveloper.pureplanting.app.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.sunrisekcdeveloper.addEdit.AddEditUi
import com.sunrisekcdeveloper.addEdit.AddEditViewModel
import com.sunrisekcdeveloper.plant.Plant
import com.sunrisekcdeveloper.pureplanting.app.navigation.ComposeKey
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackcomposeintegration.services.rememberService
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import com.zhuinden.simplestackextensions.servicesktx.rebind
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class AddEditKey(
    val plant: Plant? = null
) : ComposeKey() {

    @Composable
    override fun ScreenComposable(modifier: Modifier) {
        val viewModel = rememberService<AddEditViewModel>()
        AddEditUi(viewModel)
    }

    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            AddEditViewModel.Default(
                plantRepository = lookup(),
                router = object : AddEditViewModel.Router {
                    override fun jumpToRoot() {
                        backstack.jumpToRoot()
                    }

                    override fun goBack() {
                        backstack.goBack()
                    }
                },
                eventEmitter = lookup(),
                plant = plant
            ).let {  viewModel ->
                add(viewModel)
                rebind<AddEditViewModel>(viewModel)
            }
        }
    }
}