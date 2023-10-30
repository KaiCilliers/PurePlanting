package com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.features.component.plants.Plant
import com.sunrisekcdeveloper.pureplanting.navigation.ThemeSurfaceWrapper
import java.time.DayOfWeek

@Composable
fun AddEditPlantScreen(
    name: String,
    nameUpdater: (String) -> Unit,
    description: String,
    descriptionUpdater: (String) -> Unit,
    size: String,
    sizeUpdater: (String) -> Unit,
    daysToWater: List<DayOfWeek>,
    daysToWaterUpdater: (String) -> Unit,
    wateringHour: Int,
    wateringHourUpdater: (String) -> Unit,
    amountOfWater: String,
    amountOfWaterUpdater: (String) -> Unit,
    imgSrcUri: String,
    imgSrcUriUpdater: (String) -> Unit,
    onAddPlantTap: (Plant) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        LabelAndPlaceHolderTextField(text = name, onValueChanged = nameUpdater)

        Spacer(modifier = Modifier.height(12.dp))
        LabelAndPlaceHolderTextField(text = description, onValueChanged = descriptionUpdater)

        Spacer(modifier = Modifier.height(12.dp))
        LabelAndPlaceHolderTextField(text = size, onValueChanged = sizeUpdater)

        Spacer(modifier = Modifier.height(12.dp))
        LabelAndPlaceHolderTextField(text = daysToWater.toString(), onValueChanged = daysToWaterUpdater)

        Spacer(modifier = Modifier.height(12.dp))
        LabelAndPlaceHolderTextField(text = wateringHour.toString(), onValueChanged = wateringHourUpdater)

        Spacer(modifier = Modifier.height(12.dp))
        LabelAndPlaceHolderTextField(text = amountOfWater, onValueChanged = amountOfWaterUpdater)

        Spacer(modifier = Modifier.height(12.dp))
        LabelAndPlaceHolderTextField(text = imgSrcUri, onValueChanged = imgSrcUriUpdater)

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            val newPlant = Plant.createNewPlant(
                imageSrc = imgSrcUri,
                name = name,
                description = description,
                size = size,
                wateringDays = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
                wateringHour = wateringHour,
                wateringAmount = amountOfWater,
            )
            onAddPlantTap(newPlant)
        }) {
            Text(text = "Create a Plant")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelAndPlaceHolderTextField(
    text: String,
    onValueChanged: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = onValueChanged,
        label = { Text(text = "Your Label") },
        placeholder = { Text(text = "Your Placeholder/Hint") },
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun AddEditPlantScreen_Preview() {
    ThemeSurfaceWrapper {
        AddEditPlantScreen(
            name = "Pamela Salazar",
            nameUpdater = {},
            description = "posidonium",
            descriptionUpdater = {},
            size = "dolore",
            sizeUpdater = {},
            daysToWater = listOf(),
            daysToWaterUpdater = {},
            wateringHour = 9647,
            wateringHourUpdater = {},
            amountOfWater = "appetere",
            amountOfWaterUpdater = {},
            imgSrcUri = "expetendis",
            imgSrcUriUpdater = {},
            onAddPlantTap = {}
        )
    }
}