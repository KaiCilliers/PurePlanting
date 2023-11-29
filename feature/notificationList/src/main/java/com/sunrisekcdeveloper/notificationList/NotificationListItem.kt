package com.sunrisekcdeveloper.notificationList

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NextPlan
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Composable
fun NotificationListItem(
    title: String,
    time: LocalTime,
    content: String,
    seen: Boolean,
    primaryButtonText: String,
    onItemClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.NextPlan,
                contentDescription = "Icon",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = title)
                Text(text = time.format(DateTimeFormatter.ofPattern("hh:mm a")))
            }
            if (!seen) {
                Canvas(modifier = Modifier
                    .size(10.dp),
                    onDraw = {
                        drawCircle(color = Color.Red)
                    })
            }
        }
        Text(text = content)
        Button(onClick = { onItemClicked() }) {
            Text(text = primaryButtonText)
        }
    }
}

@Preview
@Composable
private fun NotificationListScreen_Preview() {
    ThemeSurfaceWrapper {
        NotificationListItem(
            title = "Daily plant notification",
            time = LocalTime.now(),
            seen = false,
            content = "Don't forget to water your plants today!",
            primaryButtonText = "Click here to view information",
            onItemClicked = {},
        )
    }
}