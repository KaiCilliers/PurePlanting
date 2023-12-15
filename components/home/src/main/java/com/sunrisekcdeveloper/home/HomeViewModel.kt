package com.sunrisekcdeveloper.home

import com.sunrisekcdeveloper.notification.domain.NotificationRepository
import com.sunrisekcdeveloper.plant.domain.PlantRepository
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
            clock
        )
    }

}