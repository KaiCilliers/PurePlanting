package com.sunrisekcdeveloper.pureplanting.features.notificationList.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.core.design.noRippleClickable
import com.sunrisekcdeveloper.pureplanting.core.design.theme.PurePlantingTheme
import com.sunrisekcdeveloper.pureplanting.R
import com.sunrisekcdeveloper.pureplanting.core.design.theme.ppColors
import com.sunrisekcdeveloper.pureplanting.core.design.theme.ppTypography
import com.sunrisekcdeveloper.pureplanting.domain.notification.Notification
import com.sunrisekcdeveloper.pureplanting.domain.notification.PlantTag
import java.time.format.DateTimeFormatter

@Composable
internal fun NotificationListItem(
    notification: Notification,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface (
        color = MaterialTheme.ppColors.surface,
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable { onClick() }
    ){
        Column (
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(vertical = 12.dp)
                .background(MaterialTheme.ppColors.surface)
        ){
            TopSection(
                title = notification.type.title,
                time = notification.created.format(DateTimeFormatter.ofPattern("hh:mm a")), // create formatter somewhere and reuse
                seen = notification.seen
            )
            BottomSection(
                body = notification.type.body,
                targetPlants = notification.type.targetPlants,
            )
        }

    }
}

@Composable
private fun TopSection(
    title: String,
    time: String,
    seen: Boolean,
) {
    Row (
        verticalAlignment = Alignment.CenterVertically
    ){
        PlantIcon()
        Spacer(modifier = Modifier.weight(0.05f))
        Column {
            Text(
                text = title,
                color = MaterialTheme.ppColors.onSurfacePrimary,
                style = MaterialTheme.ppTypography.captionMedium
            )

            Text(
                text = time,
                color = MaterialTheme.ppColors.onSurfaceSecondary,
                style = MaterialTheme.ppTypography.captionSmall
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (!seen) {
            Canvas(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.Top), // todo align with baseline of text in row
                onDraw = {
                    drawCircle(color = Color.Red)
                }
            )
        }
    }
}

@Composable
private fun BottomSection(
    body: String,
    targetPlants: List<String>,
) {

    fun <T> List<T>.isOfSizeOne(): Boolean {
        return this.size == 1
    }

    Text(
        text = body,
        color = MaterialTheme.ppColors.onSurfaceSecondary,
        style = MaterialTheme.ppTypography.bodySmall
    )
    Text(
        text = if (targetPlants.isOfSizeOne()) "Go to Plant" else "Click here to view information", // todo string resource
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.ppTypography.bodyMedium,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun PlantIcon (
    modifier: Modifier = Modifier
){
    Surface (
        color = MaterialTheme.ppColors.primaryMuted,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.size(40.dp) // todo make dynamic scalable based on height of other elements in a row
    ){
        Image(
            painter = painterResource(id = R.drawable.single_plant_placeholder),
            contentDescription = "",
            modifier = Modifier.padding(5.dp)
        )
    }
}

@Preview
@Composable
private fun NotificationListItem_Preview(
    @PreviewParameter(NotificationPreviewParameterProvider::class) notification: Notification
) {
    PurePlantingTheme {
        NotificationListItem(notification, onClick = {})
    }
}

private class NotificationPreviewParameterProvider : PreviewParameterProvider<Notification> {
    override val values = sequenceOf(
        Notification.createForgotToWater(listOf(PlantTag("1", "name"))).copy(seen = true),
        Notification.createForgotToWater(listOf(PlantTag("1", "name"), PlantTag("2", "name"))),
        Notification.createWaterSoon(listOf(PlantTag("1", "name"))),
        Notification.createWaterSoon(listOf(PlantTag("1", "name"), PlantTag("2", "name"))),
    )
}