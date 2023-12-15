package com.sunrisekcdeveloper.plantList

import com.sunrisekcdeveloper.notification.domain.NotificationRepository
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import java.time.Clock

interface MainComponent {

    val notificationIconComponent: NotificationIconComponent

    val plantListViewModel: PlantListViewModel

    class Fake : MainComponent {

        override val notificationIconComponent = NotificationIconComponent.Fake()

        override val plantListViewModel = PlantListViewModel.Fake()

    }

    class Default(
        plantRepository: PlantRepository,
        notificationRepository: NotificationRepository,
        plantListViewModelRouter: PlantListViewModel.Router,
        notificationIconComponentRouter: NotificationIconComponent.Router,
        clock: Clock = Clock.systemDefaultZone(),
        notificationIconComp: NotificationIconComponent? = null,
        plantListComp: PlantListViewModel? = null,
    ) : MainComponent {

        override val notificationIconComponent = notificationIconComp ?: NotificationIconComponent.Default(
            notificationRepository,
            notificationIconComponentRouter
        )

        override val plantListViewModel = plantListComp ?: PlantListViewModel.Default(
            plantRepository,
            plantListViewModelRouter,
            clock
        )
    }

}