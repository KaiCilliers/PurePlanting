package com.sunrisekcdeveloper.pureplanting.features.presentation.plantdetail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.features.component.plants.Plant
import com.sunrisekcdeveloper.pureplanting.navigation.ThemeSurfaceWrapper
import java.time.DayOfWeek

@Composable
fun PlantDetailScreen(
    plant: State<Plant>,
    onWateredButtonTapped: (Plant) -> Unit,
    onEditPlantTapped: (Plant) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Image Source:", modifier.padding(20.dp))
            LargeText(text = plant.value.details.imageSrcUri)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Name:", modifier.padding(20.dp))
            LargeText(text = plant.value.details.name)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Desc:", modifier.padding(20.dp))
            LargeText(text = plant.value.details.description)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Size:", modifier.padding(20.dp))
            LargeText(text = plant.value.details.size)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Frequency:", modifier.padding(20.dp))
            LargeText(text = "${plant.value.wateringInfo.days.size} times/week")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Watered?:", modifier.padding(20.dp))
            LargeText(text = plant.value.hasBeenWatered.toString())
        }

        Button(onClick = { onEditPlantTapped(plant.value) }) {
            Text(text = "Edit Plant")
        }

        if(!plant.value.hasBeenWatered) {
            Button(onClick = { onWateredButtonTapped(plant.value) }) {
                Text(text = "Mark as watered")
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun PlantDetailScreen_Preview() {
    ThemeSurfaceWrapper {
        val plant = Plant.createNewPlant(
            imageSrc = "melius",
            name = "Leo Velasquez",
            description = "velit",
            size = "medium",
            wateringDays = listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
            wateringHour = 12,
            wateringAmount = "250ml"
        )

        PlantDetailScreen(
            plant = mutableStateOf(plant),
            onWateredButtonTapped = {},
            onEditPlantTapped = {}
        )
    }
}