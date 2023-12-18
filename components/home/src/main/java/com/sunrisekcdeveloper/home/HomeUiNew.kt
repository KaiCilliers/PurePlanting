package com.sunrisekcdeveloper.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.home.ui.NotificationIcon
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper

@Composable
fun HomeUiNew() {
    Column {
        Row(
            Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 35.dp)
                .wrapContentHeight()
        ) {
            Text(
                text = "Let's care\nMy Plants!",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            NotificationIcon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(50.dp), // todo update notification icon to scale predictably (try to set size smaller)
                showBadge = true,
                onClick = { }
            )
        }
        PlantListUiNew(viewModel = PlantListViewModel.Fake())
    }
}

@Preview
@Composable
private fun HomeUiNew_Preview() {
    ThemeSurfaceWrapper {
        HomeUiNew()
    }
}