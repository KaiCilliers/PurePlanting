package com.sunrisekcdeveloper.pureplanting.features.home.ui

import android.annotation.SuppressLint
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sunrisekcdeveloper.pureplanting.core.design.theme.PurePlantingTheme
import com.sunrisekcdeveloper.pureplanting.domain.plant.Plant
import com.sunrisekcdeveloper.pureplanting.R
import com.sunrisekcdeveloper.pureplanting.features.home.subcomponents.PlantListViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// todo add leakcanary to project
// todo image size reduction before saving and using
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlantCard(
    plant: Plant,
    needsWater: Boolean,
    onWaterToggleClick: (needsWater: Boolean) -> Unit,
    onDeletePlant: () -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    var showOptionsMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .wrapContentSize()
            .combinedClickable(
                onClick = { onCardClick() },
                onLongClick = { showOptionsMenu = true }
            ),
        shape = RoundedCornerShape(8.dp),
//        border = BorderStroke(
//            width = 1.dp,
//            color = neutralus300.copy(alpha = 0.2f)
//        )
    ) {
        Column(
            Modifier.fillMaxWidth()
        ) {
            TopSection(
                plant = plant,
                modifier = Modifier.fillMaxWidth()
            )
            BottomSection(
                title = plant.details.name,
                description = plant.details.description,
                needsWater = needsWater,
                onButtonClick = { onWaterToggleClick(needsWater) },
            )
        }
        DropdownMenu(
            expanded = showOptionsMenu,
            onDismissRequest = { showOptionsMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = "Delete Plant") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "",
                    )

                },
                onClick = {
                    showOptionsMenu = false
                    onDeletePlant()
                }
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
    val dateNeedsWater = remember { plant.currentWateringDate(now) }

    Box(
        modifier = modifier
//            .background(otherOlive500.copy(alpha = 0.3f))
            .aspectRatio(1f)
    ) {
        val imageSrc = plant.details.imageSrcUri
        if (!imageSrc.isNullOrBlank()) {
            AsyncImage(
                model = Uri.parse(imageSrc),
                contentDescription = "",
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
                placeholder = debugPlaceholder(R.drawable.preview_plant),
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.single_plant_placeholder),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.Center)
                    .scale(0.8f)
            )
        }

        Column(
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Tag(
                label = plant.wateringInfo.amount,
                modifier = Modifier.padding(start = 12.dp, top = 12.dp)
            )
            Tag(
                // todo string resources
                label = when (dateNeedsWater.dayOfYear) {
                    now.dayOfYear -> "Today"
                    now.dayOfYear.minus(1) -> "Yesterday"
                    else -> dateFormatter.format(plant.currentWateringDate(now))
                },
                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun debugPlaceholder(@DrawableRes debugPreview: Int) =
    if (LocalInspectionMode.current) {
        painterResource(id = debugPreview)
    } else {
        null
    }

@Composable
private fun Tag(
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
//        color = neutralus500.copy(alpha = 0.7f),
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
//                color = neutralus500, // todo use MaterialTheme color
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
@Preview(widthDp = 200)
@Composable
private fun PlantCard_Preview() {
    PurePlantingTheme {
        val plant = PlantListViewModel.Fake().plants.value.first()
        val now = remember { LocalDateTime.now() }
        val needsWater = remember(now.dayOfYear) { plant.needsWater(now) }

        PlantCard(
            plant = plant,
            needsWater = needsWater,
            onWaterToggleClick = {},
            onDeletePlant = {},
            onCardClick = {},
        )
    }
}