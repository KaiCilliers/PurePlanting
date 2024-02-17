package com.sunrisekcdeveloper.pureplanting.core.navigation

import com.sunrisekcdeveloper.pureplanting.core.alarm.AlarmScheduler
import com.sunrisekcdeveloper.pureplanting.core.design.ui.SnackbarEmitter
import com.sunrisekcdeveloper.pureplanting.domain.plant.Plant
import com.sunrisekcdeveloper.pureplanting.domain.plant.PlantRepository
import com.sunrisekcdeveloper.pureplanting.features.addEdit.AddEditKey
import com.sunrisekcdeveloper.pureplanting.features.detail.DetailKey
import com.sunrisekcdeveloper.pureplanting.features.home.HomeViewModel
import com.sunrisekcdeveloper.pureplanting.features.notificationList.NotificationListKey
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
                Scopes.HOME -> {
                    val plantListRouter = object : HomeViewModel.Router {
                        override fun goToAddPlant() {
                            backstack.goTo(AddEditKey())
                        }

                        override fun goToPlantDetail(plant: Plant) {
                            backstack.goTo(DetailKey(plant))
                        }

                        override fun goToNotificationList() {
                            backstack.goTo(NotificationListKey)
                        }

                    }
                    HomeViewModel.Default(
                        plantRepository = lookup<PlantRepository>(),
                        router = plantListRouter,
                        notificationRepository = lookup(),
                        eventEmitter = lookup<SnackbarEmitter>(),
                        alarmScheduler = lookup<AlarmScheduler>()
                    ).run {
                        add(this)
                        rebind<HomeViewModel>(this)
                    }
                }
            }
        }
    }

    object Scopes {
        const val HOME = "home"
    }
}