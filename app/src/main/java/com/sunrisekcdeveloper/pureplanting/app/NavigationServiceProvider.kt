package com.sunrisekcdeveloper.pureplanting.app

import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import com.sunrisekcdeveloper.plantList.DefaultPlantListComponent
import com.sunrisekcdeveloper.plantList.PlantListComponent
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
                    val plantListRouter = object : PlantListComponent.Router {
                        override fun goToAddPlant() {
                            backstack.goTo(AddEditKey())
                        }

                        override fun goToPlantDetail(plant: Plant) {
                            backstack.goTo(DetailKey(plant))
                        }

                    }
                    DefaultPlantListComponent(
                        plantRepository = lookup<PlantRepository>(),
                        router = plantListRouter
                    ).run {
                        add(this)
                        rebind<PlantListComponent>(this)
                    }
                }
            }
        }
    }

    object Scopes {
        const val NOTIFICATION = "notification"
    }
}