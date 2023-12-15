package com.sunrisekcdeveloper.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper

@Composable
fun HomeUi(viewModel: HomeViewModel) {
    Column {
        NotificationIconUi(viewModel.notificationIconViewModel)
        PlantListUi(viewModel.plantListViewModel)
    }
}

@Preview
@Composable
private fun HomeUi_Preview() {
    ThemeSurfaceWrapper {
        HomeUi(HomeViewModel.Fake())
    }
}