package com.sunrisekcdeveloper.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlantCard(
    plant: Plant,
    onCardClick: () -> Unit,
    onWaterClicked: () -> Unit,
    onUndoWaterClicked: () -> Unit,
    onDeletePlant: () -> Unit,
    modifier: Modifier = Modifier
) {

    val needsWaterToday = plant.needsWaterToday(LocalDateTime.now())
    val dateNeedWater = if (needsWaterToday) LocalDateTime.now() else plant.dateNeededWaterBefore(LocalDateTime.now())
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd-MM") }
    var showOptionsMenu by remember { mutableStateOf(false) }

    Box(modifier = modifier
        .fillMaxSize()
        .combinedClickable(
            onClick = { onCardClick() },
            onLongClick = { showOptionsMenu = true }
        )) {
        Column {
            Text(text = plant.wateringInfo.amount)
            Text(
                text = if (needsWaterToday) {
                    "Today"
                } else if (dateNeedWater?.dayOfYear == LocalDateTime.now().minusDays(1).dayOfYear) {
                    "Yesterday"
                } else {
                    if (dateNeedWater != null) {
                        dateFormatter.format(dateNeedWater)
                    } else ""
                }
            )
            Row(
                modifier = Modifier.background(Color.Gray)
            ) {
                Column(
                    modifier.weight(1f)
                ) {
                    Text(text = plant.details.name)
                    Text(text = plant.details.description)
                }
                Icon(
                    imageVector = if (needsWaterToday) Icons.Outlined.AcUnit else Icons.Outlined.AddTask,
                    contentDescription = "water",
                    modifier = modifier
                        .size(30.dp)
                        .clickable { if (needsWaterToday) onWaterClicked() else onUndoWaterClicked() }
                )
            }
        }
        DropdownMenu(
            expanded = showOptionsMenu,
            onDismissRequest = { showOptionsMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = "Delete Plant") },
                onClick = {
                    showOptionsMenu = false
                    onDeletePlant()
                }
            )
        }
    }
}

@Preview
@Composable
private fun PlantCard_Preview() {
    ThemeSurfaceWrapper {
        PlantCard(
            Plant.createNewPlant(
                imageSrc = "",
                name = "Monstera",
                description = "Short Description",
                size = "Medium",
                wateringDays = listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY),
                wateringTime = LocalTime.now(),
                wateringAmount = "50ml",
                createdAt = LocalDateTime.now().minusDays(1)
            ),
            onWaterClicked = {},
            onCardClick = {},
            onUndoWaterClicked = {},
            onDeletePlant = {}
        )
    }
}