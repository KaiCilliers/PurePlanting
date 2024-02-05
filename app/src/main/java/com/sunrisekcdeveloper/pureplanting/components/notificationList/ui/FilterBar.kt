package com.sunrisekcdeveloper.pureplanting.components.notificationList.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.library.design.noRippleClickable
import com.sunrisekcdeveloper.pureplanting.library.design.theme.accent500
import com.sunrisekcdeveloper.pureplanting.library.design.theme.neutralus300
import com.sunrisekcdeveloper.pureplanting.components.notificationList.models.NotificationFilter

@Composable
internal fun FilterBar(
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
            selected = selectedFilter == NotificationFilter.ALL,
            onClick = { onSelection(NotificationFilter.ALL) }
        )
        Spacer(modifier = Modifier.weight(0.2f))

        FilterBarItem(
            text = "Forgot to Water",
            selected = selectedFilter == NotificationFilter.FORGOT_TO_WATER,
            onClick = { onSelection(NotificationFilter.FORGOT_TO_WATER) }
        )
        Spacer(modifier = Modifier.weight(0.2f))

        FilterBarItem(
            text = "Need Water",
            selected = selectedFilter == NotificationFilter.NEEDS_WATER,
            onClick = { onSelection(NotificationFilter.NEEDS_WATER) }
        )
        Spacer(modifier = Modifier.weight(0.6f))
    }
}