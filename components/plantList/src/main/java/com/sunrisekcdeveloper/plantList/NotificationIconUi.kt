package com.sunrisekcdeveloper.plantList

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationIconUi(
    viewModel: NotificationIconViewModel,
    modifier: Modifier = Modifier,
) {

    val isBadgeVisible by viewModel.isNotificationBadgeVisible.collectAsState()

    Box(modifier = modifier.wrapContentSize()) {
        Icon(
            imageVector = Icons.Filled.Notifications,
            contentDescription = "notifications",
            modifier = modifier
                .size(40.dp)
                .clickable {
                    viewModel.onIconClick()
                }
        )
        if (isBadgeVisible) {
            Canvas(
                modifier = Modifier
                    .size(10.dp)
                    .align(Alignment.TopEnd),
                onDraw = {
                drawCircle(color = Color.Red)
            })
        }
    }
}

@Preview
@Composable
private fun NotificationIconUi_Preview() {
    ThemeSurfaceWrapper {
        NotificationIconUi(NotificationIconViewModel.Fake())
    }
}