package com.sunrisekcdeveloper.pureplanting.navigation

import com.sunrisekcdeveloper.design.ui.SnackbarEmitter
import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import com.sunrisekcdeveloper.home.subcomponents.PlantListViewModel
import com.sunrisekcdeveloper.pureplanting.features.AddEditKey
import com.sunrisekcdeveloper.pureplanting.features.DetailKey
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackextensions.services.DefaultServiceProvider
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import com.zhuinden.simplestackextensions.servicesktx.rebind

class NavigationServiceProvider : DefaultServiceProvider() {
    override fun bindServices(serviceBinder: ServiceBinder) {
        super.bindServices(serviceBinder)

        val scope = serviceBinder.scopeTag

        with(serviceBinder) {
            when (scope) {
                Scopes.NOTIFICATION -> {
                    val plantListRouter = object : PlantListViewModel.Router {
                        override fun goToAddPlant() {
                            backstack.goTo(AddEditKey())
                        }

                        override fun goToPlantDetail(plant: Plant) {
                            backstack.goTo(DetailKey(plant))
                        }

                    }
                    PlantListViewModel.Default(
                        plantRepository = lookup<PlantRepository>(),
                        router = plantListRouter,
                        eventEmitter = lookup<SnackbarEmitter>()
                    ).run {
                        add(this)
                        rebind<PlantListViewModel>(this)
                    }
                }
            }
        }
    }

    object Scopes {
        const val NOTIFICATION = "notification" // todo rename to Home since plantList is being shared
    }
}