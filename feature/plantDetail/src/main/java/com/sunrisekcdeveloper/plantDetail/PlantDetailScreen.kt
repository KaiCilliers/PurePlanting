package com.sunrisekcdeveloper.plantDetail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sunrisekcdeveloper.plant.Plant
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PlantDetailScreen(
    plant: State<Plant>,
    onWateredButtonTapped: () -> Unit,
    onEditPlantTapped: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
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
            LargeText(text = plant.value.wateringInfo.days.joinToString(", ") {
                it.name.lowercase().replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
            }
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Time", modifier.padding(20.dp))
            LargeText(text = plant.value.wateringInfo.time.format(timeFormatter))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Watered amount:", modifier.padding(20.dp))
            LargeText(text = plant.value.wateringInfo.amount)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Needs water:", modifier.padding(20.dp))
            LargeText(text = plant.value.needsWater(LocalDateTime.now()).toString())
        }

        Button(onClick = { onEditPlantTapped() }) {
            Text(text = "Edit Plant")
        }

        if (plant.value.needsWater(LocalDateTime.now())) {
            Button(onClick = { onWateredButtonTapped() }) {
                Text(text = "Mark as watered")
            }
        }
    }
}

// todo remove below
@Composable
private fun LargeText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 42.sp,
        modifier = modifier,
    )
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
            size = "Medium",
            wateringDays = listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
            wateringTime = LocalTime.now(),
            wateringAmount = "240 ml"
        )

        PlantDetailScreen(
            plant = mutableStateOf(plant),
            onWateredButtonTapped = {},
            onEditPlantTapped = {}
        )
    }
}