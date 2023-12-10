package com.sunrisekcdeveloper.plantList

interface MainComponent {

    val notificationIconComponent: NotificationIconComponent

    val plantListComponent: PlantListComponent

    class Fake : MainComponent {

        override val notificationIconComponent = NotificationIconComponent.Fake()

        override val plantListComponent = PlantListComponent.Fake()

    }

}