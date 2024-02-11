package com.sunrisekcdeveloper.pureplanting.features.notificationList.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.features.notificationList.headingPadding
import com.sunrisekcdeveloper.pureplanting.features.notificationList.models.NotificationFilter

@Composable
internal fun Header(
    selectedFilter: NotificationFilter,
    onFilterChange: (NotificationFilter) -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.padding(headingPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
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