package com.sunrisekcdeveloper.pureplanting.features

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.detail.DetailViewModel
import com.sunrisekcdeveloper.detail.Router
import com.sunrisekcdeveloper.navigation.FragmentKey
import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import com.zhuinden.simplestackextensions.servicesktx.rebind
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailKey(
    val plant: Plant
) : FragmentKey() {
    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            DetailViewModel.Default(
                plant,
                lookup<PlantRepository>(),
                router = object : Router {
                    override fun goToEditPlant(plant: Plant) {
                        backstack.goTo(AddEditKey(plant))
                    }
                }
            ).let { viewModel ->
                add(viewModel)
                rebind<DetailViewModel>(viewModel)
            }
        }
    }

    override fun instantiateFragment(): Fragment = DetailFragment()
}