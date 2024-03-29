package com.sunrisekcdeveloper.pureplanting.features.notificationList.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.core.design.theme.PurePlantingTheme
import com.sunrisekcdeveloper.pureplanting.core.design.theme.ppColors
import com.sunrisekcdeveloper.pureplanting.core.design.theme.ppTypography
import com.sunrisekcdeveloper.pureplanting.domain.notification.Notification
import com.sunrisekcdeveloper.pureplanting.features.notificationList.NotificationGroupedByDay
import com.sunrisekcdeveloper.pureplanting.features.notificationList.NotificationListViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NotificationList(
    notifications: NotificationGroupedByDay,
    onItemClick: (Notification) -> Unit,
) {

    val today by remember { mutableStateOf(LocalDateTime.now()) }

    val titleListItemPadding = Modifier
        .padding(top = 16.dp)
        .padding(start = 20.dp)
    val notificationListItemPadding = Modifier
        .padding(top = 8.dp)

    LazyColumn(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .fillMaxHeight()
    ) {
        // todo replace with sticky header
        notifications.forEach { entry ->
            item(entry.key) {
                Text(
                    text = when {
                        entry.key.first == today.dayOfYear && entry.key.second == today.year -> "Today"
                        entry.key.first == today.minusDays(1).dayOfYear && entry.key.second == today.minusDays(1).year -> "Yesterday"
                        else -> LocalDate.ofYearDay(entry.key.second, entry.key.first).format(DateTimeFormatter.ofPattern("dd MMMM"))
                    },
                    color = MaterialTheme.ppColors.onSurfaceSecondary,
                    style = MaterialTheme.ppTypography.bodySmall,
                    modifier = titleListItemPadding.animateItemPlacement()
                )
            }
            itemsIndexed(entry.value, key = { _, item -> item.id }) { index, item ->
                NotificationListItem(
                    notification = item,
                    onClick = { onItemClick(item) },
                    modifier = notificationListItemPadding
                )
                if (index < entry.value.lastIndex) Divider(color = Color.Gray.copy(alpha = 0.05f), thickness = 1.dp)
            }
        }
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }

}

@SuppressLint("StateFlowValueCalledInComposition")
@Preview(showBackground = true)
@Composable
fun Preview_NotificationList() {
    PurePlantingTheme {
        NotificationList(
            notifications = NotificationListViewModel.Fake().notifications.value,
            onItemClick = {}
        )
    }
}