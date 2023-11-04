package com.sunrisekcdeveloper.pureplanting.features.presentation.plants

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sunrisekcdeveloper.pureplanting.BuildConfig
import com.sunrisekcdeveloper.pureplanting.features.component.plants.Plant
import com.sunrisekcdeveloper.pureplanting.navigation.ThemeSurfaceWrapper
import java.time.DayOfWeek

@Composable
fun PlantsScreen(
    plants: State<List<Plant>>,
    plantsFilterOption: List<PlantListFilter>,
    selectedFilter: State<PlantListFilter>,
    onFilterSelected: (PlantListFilter) -> Unit,
    onPlantTapped: (Plant) -> Unit,
    onNotificationIconTapped: () -> Unit,
    onAddIconTapped: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = "Build Type: ${BuildConfig.BUILD_TYPE}",
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
        Row(modifier) {
            plantsFilterOption.forEach {
                TabFilterOption(
                    text = it.name,
                    isSelected = selectedFilter.value == it,
                    modifier = Modifier.clickable { onFilterSelected(it) }
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "notifications",
                modifier = Modifier
                    .padding(20.dp)
                    .size(40.dp)
                    .clickable {
                        onNotificationIconTapped()
                    }
            )
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "add",
                modifier = Modifier
                    .padding(20.dp)
                    .size(40.dp)
                    .clickable {
                        onAddIconTapped()
                    }
            )
        }

        LazyColumn {
            items(plants.value) { plant ->
                Text(
                    text = plant.details.name,
                    fontSize = 22.sp,
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable { onPlantTapped(plant) }
                )
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun PlantsScreen_Preview() {
    ThemeSurfaceWrapper {
        var selectedFilter by remember { mutableStateOf(PlantListFilter.UPCOMING) }

        val plants = (1..40).map {
            Plant.createNewPlant(
                imageSrc = "cursus",
                name = "Plant #$it",
                description = "convallis",
                size = "dico",
                wateringDays = listOf(DayOfWeek.MONDAY),
                wateringHour = 1,
                wateringAmount = "aliquam"
            )
        }

        PlantsScreen(
            plantsFilterOption = listOf(PlantListFilter.UPCOMING, PlantListFilter.FORGOT_TO_WATER, PlantListFilter.HISTORY),
            selectedFilter = mutableStateOf(selectedFilter),
            onFilterSelected = { selectedFilter = it },
            plants = mutableStateOf(plants),
            onPlantTapped = {},
            onAddIconTapped = {},
            onNotificationIconTapped = {},
        )
    }
}