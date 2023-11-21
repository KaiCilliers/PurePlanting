package com.sunrisekcdeveloper.pureplanting.features.presentation.notifications

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationDomain
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.PlantNotificationType
import com.sunrisekcdeveloper.pureplanting.features.presentation.plants.TabFilterOption
import com.sunrisekcdeveloper.pureplanting.navigation.ThemeSurfaceWrapper
import java.time.LocalDateTime

@Composable
fun NotificationListScreen(
    filterOptions: List<NotificationFilter>,
    selectedFilter: State<NotificationFilter>,
    onFilterSelected: (NotificationFilter) -> Unit,
    notifications: State<NotificationsGroupedByDay>,
    onNotificationClick: (NotificationDomain) -> Unit,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "Go Back",
                modifier = Modifier
                    .size(42.dp)
                    .clickable { onBackButtonClick() }
            )
            Text(
                text = "Notifications",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f),
            )
        }
        Row {
            filterOptions.forEach {
                TabFilterOption(
                    text = it.name,
                    isSelected = selectedFilter.value == it,
                    modifier = Modifier.clickable { onFilterSelected(it) }
                )
            }
        }
        LazyColumn {
            notifications.value.forEach { entry ->
                item {
                    Text(
                        text = "${entry.key}",
                        fontSize = 42.sp
                    )
                }
                items(entry.value) { item ->
                    val btnText = if (item.type.targetPlants.size > 1) {
                        "Click here to view information"
                    } else "Go to the plant"
                    NotificationListItem(
                        title = item.type.notificationTitle,
                        time = item.created.toLocalTime(),
                        content = item.type.notificationContent,
                        primaryButtonText = btnText,
                        seen = item.seen,
                        onItemClicked = { onNotificationClick(item) }
                    )
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun NotificationListScreen_Preview() {
    ThemeSurfaceWrapper {
        var selectedFilter by remember { mutableStateOf(NotificationFilter.ALL) }
        val todayDayOfYear = LocalDateTime.now().dayOfYear
        val todayYear = LocalDateTime.now().year

        NotificationListScreen(
            filterOptions = listOf(NotificationFilter.ALL, NotificationFilter.FORGOT_TO_WATER, NotificationFilter.NEEDS_WATER),
            selectedFilter = mutableStateOf(selectedFilter),
            onFilterSelected = { selectedFilter = it },
            notifications = mutableStateOf(
                mapOf(
                    (todayDayOfYear to todayYear) to listOf(
                        NotificationDomain(
                            created = LocalDateTime.now(),
                            seen = false,
                            type = PlantNotificationType.NeedsWater(
                                targetPlants = listOf("#1", "#2"),
                                notificationContent = "Don't forget to water your plants today!"
                            )
                        ),
                        NotificationDomain(
                            created = LocalDateTime.now(),
                            seen = false,
                            type = PlantNotificationType.NeedsWater(
                                targetPlants = listOf("#1"),
                                notificationContent = "Don't forget to water your plants today!"
                            )
                        )
                    ),
                    (todayDayOfYear - 1 to todayYear) to listOf(
                        NotificationDomain(
                            created = LocalDateTime.now(),
                            seen = false,
                            type = PlantNotificationType.NeedsWater(
                                targetPlants = listOf("#1", "#2"),
                                notificationContent = "Don't forget to water your plants today!"
                            )
                        ),
                        NotificationDomain(
                            created = LocalDateTime.now(),
                            seen = false,
                            type = PlantNotificationType.NeedsWater(
                                targetPlants = listOf("#1"),
                                notificationContent = "Don't forget to water your plants today!"
                            )
                        )
                    ),
                    (todayDayOfYear to todayYear - 1) to listOf(
                        NotificationDomain(
                            created = LocalDateTime.now(),
                            seen = false,
                            type = PlantNotificationType.NeedsWater(
                                targetPlants = listOf("#1", "#2"),
                                notificationContent = "Don't forget to water your plants today!"
                            )
                        ),
                        NotificationDomain(
                            created = LocalDateTime.now(),
                            seen = false,
                            type = PlantNotificationType.NeedsWater(
                                targetPlants = listOf("#1"),
                                notificationContent = "Don't forget to water your plants today!"
                            )
                        )
                    )
                )
            ),
            onNotificationClick = {},
            onBackButtonClick = {},
        )
    }
}