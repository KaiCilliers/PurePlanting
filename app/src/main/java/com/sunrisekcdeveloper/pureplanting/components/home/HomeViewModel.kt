package com.sunrisekcdeveloper.pureplanting.components.home

import com.sunrisekcdeveloper.pureplanting.core.design.ui.SnackbarEmitter
import com.sunrisekcdeveloper.pureplanting.domain.notification.NotificationRepository
import com.sunrisekcdeveloper.pureplanting.domain.plant.PlantRepository
import com.sunrisekcdeveloper.pureplanting.components.home.subcomponents.NotificationIconViewModel
import com.sunrisekcdeveloper.pureplanting.components.home.subcomponents.PlantListViewModel
import java.time.Clock

interface HomeViewModel {

    val notificationIconViewModel: NotificationIconViewModel

    val plantListViewModel: PlantListViewModel

    class Fake : HomeViewModel {

        override val notificationIconViewModel = NotificationIconViewModel.Fake()

        override val plantListViewModel = PlantListViewModel.Fake()

    }

    class Default(
        plantRepository: PlantRepository,
        notificationRepository: NotificationRepository,
        plantListRouter: PlantListViewModel.Router,
        notificationIconRouter: NotificationIconViewModel.Router,
        clock: Clock = Clock.systemDefaultZone(),
        eventEmitter: SnackbarEmitter,
        notificationIconComp: NotificationIconViewModel? = null,
        plantListComp: PlantListViewModel? = null,
    ) : HomeViewModel {

        override val notificationIconViewModel = notificationIconComp ?: NotificationIconViewModel.Default(
            notificationRepository,
            notificationIconRouter
        )

        override val plantListViewModel = plantListComp ?: PlantListViewModel.Default(
            plantRepository,
            plantListRouter,
            eventEmitter,
            clock,
        )
    }

}