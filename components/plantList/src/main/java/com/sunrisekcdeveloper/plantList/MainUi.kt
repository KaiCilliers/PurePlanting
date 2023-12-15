package com.sunrisekcdeveloper.plantList

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper

@Composable
fun MainUi(viewModel: HomeViewModel) {
    Column {
        NotificationIconUi(viewModel.notificationIconViewModel)
        PlantListUi(viewModel.plantListViewModel)
    }
}

@Preview
@Composable
private fun MainUi_Preview() {
    ThemeSurfaceWrapper {
        MainUi(HomeViewModel.Fake())
    }
}