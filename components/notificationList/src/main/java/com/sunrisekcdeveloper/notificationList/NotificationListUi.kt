package com.sunrisekcdeveloper.notificationList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.design.noRippleClickable
import com.sunrisekcdeveloper.design.theme.accent500
import com.sunrisekcdeveloper.design.theme.neutralus100
import com.sunrisekcdeveloper.design.theme.neutralus300
import com.sunrisekcdeveloper.design.theme.neutralus500
import com.sunrisekcdeveloper.design.theme.otherOlive500
import com.sunrisekcdeveloper.design.ui.BackIcon
import com.sunrisekcdeveloper.design.ui.BoxWithBottomFade
import com.sunrisekcdeveloper.models.NotificationFilter
import com.sunrisekcdeveloper.models.NotificationFilter.ALL
import com.sunrisekcdeveloper.models.NotificationFilter.FORGOT_TO_WATER
import com.sunrisekcdeveloper.models.NotificationFilter.NEEDS_WATER
import com.sunrisekcdeveloper.notification.domain.Notification
import com.sunrisekcdeveloper.notificationList.ui.NotificationListItem
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.sunrisekcdeveloper.library.design.R as designR

@Composable
fun NotificationListUi(
    viewModel: NotificationListViewModel
) {

    val notifications by viewModel.notifications.collectAsState()
    val filter by viewModel.filter.collectAsState()

    BoxWithBottomFade(
        Modifier.background(otherOlive500.copy(alpha = 0.7f)) // todo uniform background color for screens, ie MaterialTheme :)
    ) {
        Image(
            painter = painterResource(id = designR.drawable.banner_plant),
            contentDescription = ""
        )
        Column {
            Header(
                selectedFilter = filter,
                onFilterChange = { viewModel.onFilterChanged(it) },
                onBackClick = { viewModel.onBackClick() }
            )
            Surface(
                color = neutralus100,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                NotificationList(
                    notifications = notifications,
                    onItemClick = { viewModel.onNotificationClick(it) }
                )
            }
        }
    }
}


@Composable
private fun Header(
    selectedFilter: NotificationFilter,
    onFilterChange: (NotificationFilter) -> Unit,
    onBackClick: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .padding(start = 20.dp)
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIcon(
                onClick = { onBackClick() },
            )
            Text(
                text = "Notifications",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
            )
        }
        FilterBar(
            selectedFilter = selectedFilter,
            onSelection = { onFilterChange(it) },
            modifier = Modifier.padding(vertical = 20.dp)
        )
    }
}

// todo move to design module without dependency on enum
@Composable
private fun FilterBar(
    selectedFilter: NotificationFilter,
    onSelection: (NotificationFilter) -> Unit,
    modifier: Modifier = Modifier
) {

    @Composable
    fun FilterBarItem(
        text: String,
        selected: Boolean,
        onClick: () -> Unit,
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Min)
        ) {
            Text(
                text = text,
                color = if (selected) accent500 else neutralus300,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .noRippleClickable { onClick() },
            )
            if (selected) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth(0.5f)
                        .clip(CircleShape)
                        .background(accent500)
                )
            }
        }
    }

    Row(
        modifier = modifier
            .wrapContentWidth()
            .padding(start = 20.dp),
    ) {
        FilterBarItem(
            text = "All Notifications", // todo string resource
            selected = selectedFilter == ALL,
            onClick = { onSelection(ALL) }
        )
        Spacer(modifier = Modifier.weight(0.2f))

        FilterBarItem(
            text = "Forgot to Water",
            selected = selectedFilter == FORGOT_TO_WATER,
            onClick = { onSelection(FORGOT_TO_WATER) }
        )
        Spacer(modifier = Modifier.weight(0.2f))

        FilterBarItem(
            text = "Need Water",
            selected = selectedFilter == NEEDS_WATER,
            onClick = { onSelection(NEEDS_WATER) }
        )
        Spacer(modifier = Modifier.weight(0.6f))
    }
}

// todo toolbar ignore safe areas
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NotificationList(
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
        modifier = Modifier.fillMaxHeight()
    ) {
        notifications.forEach { entry ->
            item(entry.key) {
                Text(
                    text = when {
                        entry.key.first == today.dayOfYear && entry.key.second == today.year -> "Today"
                        entry.key.first == today.minusDays(1).dayOfYear && entry.key.second == today.minusDays(1).year -> "Yesterday"
                        else -> LocalDate.ofYearDay(entry.key.second, entry.key.first).format(DateTimeFormatter.ofPattern("dd MMMM"))
                    },
                    color = neutralus500,
                    style = MaterialTheme.typography.displaySmall,
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
    }

}

@Preview
@Composable
private fun NotificationListUi_Preview() {
    ThemeSurfaceWrapper {
        NotificationListUi(NotificationListViewModel.Fake())
    }
}