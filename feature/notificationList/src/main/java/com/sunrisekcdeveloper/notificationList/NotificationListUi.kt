package com.sunrisekcdeveloper.notificationList

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sunrisekcdeveloper.ui.TabFilterOption
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper

@Composable
fun NotificationListUi(
    component: NotificationListComponent,
    modifier: Modifier = Modifier
) {

    val notifications = component.notifications.collectAsState()
    val filter = component.filter.collectAsState()

    Column(
        modifier = modifier
    ) {
        Row {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "Go Back",
                modifier = Modifier
                    .size(42.dp)
                    .clickable {
                        component.onBackClick()
                    }
            )
            Text(
                text = "Notifications",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f),
            )
        }
        Row {
            NotificationFilter.values().forEach {
                TabFilterOption(
                    text = it.name,
                    isSelected = filter.value == it,
                    modifier = Modifier.clickable { component.onFilterChanged(it) }
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
                        onItemClicked = { component.onNotificationClick(item) }
                    )
                }
            }
        }
    }

}

@Preview
@Composable
private fun NotificationListUi_Preview() {
    ThemeSurfaceWrapper {
        NotificationListUi(NotificationListComponent.Fake())
    }
}