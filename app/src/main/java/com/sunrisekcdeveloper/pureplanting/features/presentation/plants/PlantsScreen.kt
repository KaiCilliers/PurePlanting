package com.sunrisekcdeveloper.pureplanting.features.presentation.plants

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sunrisekcdeveloper.pureplanting.navigation.ThemeSurfaceWrapper

@Composable
fun PlantsScreen(
    plantsFilterOption: List<PlantListFilter>,
    selectedFilter: State<PlantListFilter>,
    onFilterSelected: (PlantListFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        plantsFilterOption.forEach {
            TabFilterOption(
                text = it.name,
                isSelected = selectedFilter.value == it,
                modifier = Modifier.clickable { onFilterSelected(it) }
            )
        }
    }
}

@Composable
fun TabFilterOption(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {

    val textColor = if (isSelected) Color.Blue else Color.Gray
    val textAlpha = if (isSelected) 1.0f else 0.5f

    Text(
        text = text,
        color = textColor,
        fontSize = 18.sp,
        modifier = modifier
            .alpha(textAlpha)
            .padding(start = 12.dp)
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun PlantsScreen_Preview() {
    ThemeSurfaceWrapper {

        var selectedFilter by remember { mutableStateOf(PlantListFilter.UPCOMING) }

        PlantsScreen(
            plantsFilterOption = listOf(PlantListFilter.UPCOMING, PlantListFilter.FORGOT_TO_WATER, PlantListFilter.HISTORY),
            selectedFilter = mutableStateOf(selectedFilter),
            onFilterSelected = { selectedFilter = it }
        )
    }
}