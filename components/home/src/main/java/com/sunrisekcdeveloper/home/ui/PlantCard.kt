package com.sunrisekcdeveloper.home.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.components.home.R
import com.sunrisekcdeveloper.design.theme.PurePlantingTheme
import com.sunrisekcdeveloper.design.theme.neutralus300
import com.sunrisekcdeveloper.home.PlantListViewModel
import com.sunrisekcdeveloper.plant.domain.Plant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.sunrisekcdeveloper.library.design.R as designR

// todo add leakcanary to project
@Composable
fun PlantCard(
    plant: Plant,
    needsWater: Boolean,
    onWaterToggleClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier.wrapContentSize(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            Modifier.width(IntrinsicSize.Max)
        ) {
            TopSection(
                plant = plant,
                modifier = Modifier.fillMaxWidth()
            )
            BottomSection(
                title = plant.details.name,
                description = plant.details.description,
                needsWater = needsWater,
                onButtonClick = { onWaterToggleClick() },
                modifier = Modifier.wrapContentSize()
            )
        }
    }
}

@Composable
private fun TopSection(
    plant: Plant,
    modifier: Modifier = Modifier
) {

    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd MMM") }
    val now = remember { LocalDateTime.now() }
    val dateNeedsWater = remember { plant.dateNeededWaterBefore(now) }

    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.onBackground)
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Tag(
                label = plant.wateringInfo.amount,
                modifier = Modifier.padding(start = 12.dp, top = 12.dp)
            )
            Tag(
                // todo string resources
                label = when (dateNeedsWater?.dayOfYear) {
                    null -> "No date" // todo this scenario should not occur, see if you can move this calculation someplace else
                    now.dayOfYear -> "Today"
                    now.dayOfYear.minus(1) -> "Yesterday"
                    else -> dateFormatter.format(plant.dateNeededWaterBefore(now))
                },
                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
            )
        }

        Image(
            painter = painterResource(id = designR.drawable.single_plant_placeholder),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.Center)
                .clip(shape = RoundedCornerShape(80.dp))
        )
    }
}

@Composable
private fun Tag(
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = neutralus300,
        shape = RoundedCornerShape(5.dp),
        modifier = modifier.wrapContentSize()
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.background,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun BottomSection(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    needsWater: Boolean,
    onButtonClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )


            Text(
                text = description,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.surface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(
            modifier = modifier.weight(0.1f)
        )
        if (needsWater) {
            Image(
                painter = painterResource(id = R.drawable.water_now_icon),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.clickable { onButtonClick() },
                contentDescription = "",
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.bordered_check),
                modifier = Modifier.clickable { onButtonClick() },
                contentDescription = "",
            )
        }

    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Preview()
@Composable
private fun PlantCard_Preview() {
    PurePlantingTheme {
        val plant = PlantListViewModel.Fake().plants.value.first()
        val now = remember { LocalDateTime.now() }
        val needsWater = remember(now.dayOfYear) { plant.needsWaterToday(now) }

        PlantCard(
            plant = plant,
            needsWater = needsWater,
            onWaterToggleClick = {},
            modifier = Modifier.width(200.dp)
        )
    }
}