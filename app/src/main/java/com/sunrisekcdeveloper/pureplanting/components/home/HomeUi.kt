package com.sunrisekcdeveloper.pureplanting.components.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.library.design.ui.PlantBox
import com.sunrisekcdeveloper.pureplanting.components.home.subcomponents.NotificationIconUi
import com.sunrisekcdeveloper.pureplanting.components.home.subcomponents.PlantListUi
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper

@Composable
fun HomeUi(
    viewModel: HomeViewModel
) {
    PlantBox {
        Column {
            Row(
                Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 35.dp)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Let's Care\nMy Plants!",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                NotificationIconUi(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(42.dp), // todo update notification icon to scale predictably (try to set size smaller)
                    viewModel = viewModel.notificationIconViewModel
                )
            }
            PlantListUi(
                viewModel = viewModel.plantListViewModel,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
}

@Preview
@Composable
private fun HomeUi_Preview() {
    ThemeSurfaceWrapper {
        HomeUi(HomeViewModel.Fake())
    }
}