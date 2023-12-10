package com.sunrisekcdeveloper.plantList

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper

@Composable
fun MainUi(component: MainComponent) {
    Column {
        NotificationIconUi(component.notificationIconComponent)
        PlantListUi(component.plantListComponent)
    }
}

@Preview
@Composable
private fun MainUi_Preview() {
    ThemeSurfaceWrapper {
        MainUi(MainComponent.Fake())
    }
}