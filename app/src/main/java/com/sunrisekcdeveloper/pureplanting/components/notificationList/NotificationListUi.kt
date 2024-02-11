package com.sunrisekcdeveloper.notificationList

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.core.design.ui.BackIcon
import com.sunrisekcdeveloper.pureplanting.core.design.ui.BoxWithBottomFade
import com.sunrisekcdeveloper.pureplanting.components.notificationList.ui.Header
import com.sunrisekcdeveloper.notificationList.ui.NotificationList
import com.sunrisekcdeveloper.pureplanting.core.design.ui.ThemeSurfaceWrapper
import com.sunrisekcdeveloper.pureplanting.R

@Composable
fun NotificationListUi(
    viewModel: NotificationListViewModel
) {

    val notifications by viewModel.notifications.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    BoxWithBottomFade(
//        Modifier.background(otherOlive500.copy(alpha = 0.7f)) // todo uniform background color for screens, ie MaterialTheme :)
    ) {
        Image(
            painter = painterResource(id = R.drawable.banner_plant),
            contentDescription = ""
        )
        Box(
            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Header(
                    selectedFilter = filter,
                    onFilterChange = { viewModel.onFilterChanged(it) },
                )
                when {
                    isLoading -> {
                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp),
//                            color = accent500
                        )
                    }
                    notifications.isEmpty() -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(modifier = Modifier.weight(1f))

                            Image(
                                painter = painterResource(id = R.drawable.three_plants),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.weight(0.1f))

                            Text(
                                text = "Nothing to see here.",
                                style = MaterialTheme.typography.displayLarge,
                            )
                            Spacer(modifier = Modifier.weight(0.05f))

                            Text(
                                text = "Notifications that you receive will be placed here for you to review at any time",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(horizontal = 30.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    else -> {
                        Surface(
//                            color = neutralus100,
                            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                        ) {
                            NotificationList(
                                notifications = notifications,
                                onItemClick = { viewModel.onNotificationClick(it) }
                            )
                        }
                    }
                }
            }
            BackIcon(
                onClick = viewModel::onBackClick,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(headingPadding)
            )
        }
    }
}

@Preview
@Composable
private fun NotificationListUi_Preview() {
    ThemeSurfaceWrapper {
        NotificationListUi(NotificationListViewModel.Fake())
    }
}