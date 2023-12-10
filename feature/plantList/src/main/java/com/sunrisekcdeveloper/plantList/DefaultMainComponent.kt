package com.sunrisekcdeveloper.plantList

import com.sunrisekcdeveloper.notification.domain.NotificationRepository
import com.sunrisekcdeveloper.plant.domain.PlantRepository
import java.time.Clock

class DefaultMainComponent(
    plantRepository: PlantRepository,
    notificationRepository: NotificationRepository,
    plantListComponentRouter: PlantListComponent.Router,
    notificationIconComponentRouter: NotificationIconComponent.Router,
    clock: Clock = Clock.systemDefaultZone(),
    notificationIconComp: NotificationIconComponent? = null,
    plantListComp: PlantListComponent? = null,
) : MainComponent {

    override val notificationIconComponent = notificationIconComp ?: DefaultNotificationIconComponent(
        notificationRepository,
        notificationIconComponentRouter
    )

    override val plantListComponent = plantListComp ?: DefaultPlantListComponent(
        plantRepository,
        plantListComponentRouter,
        clock
    )
}