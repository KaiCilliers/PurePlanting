package com.sunrisekcdeveloper.pureplanting.features

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.addEdit.AddEditViewModel
import com.sunrisekcdeveloper.addEdit.Router
import com.sunrisekcdeveloper.navigation.FragmentKey
import com.sunrisekcdeveloper.plant.domain.Plant
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import com.zhuinden.simplestackextensions.servicesktx.rebind
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddEditKey(
    val plant: Plant? = null
) : FragmentKey() {
    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            AddEditViewModel.Default(
                plantRepository = lookup(),
                router = object : Router {
                    override fun jumpToRoot() {
                        backstack.jumpToRoot()
                    }
                },
                plant = plant
            ).let {  viewModel ->
                add(viewModel)
                rebind<AddEditViewModel>(viewModel)
            }
        }
    }

    override fun instantiateFragment(): Fragment = AddEditPlantFragment()
}