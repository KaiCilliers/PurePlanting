package com.sunrisekcdeveloper.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.components.home.R
import com.sunrisekcdeveloper.design.theme.accent500
import com.sunrisekcdeveloper.design.theme.neutralus300
import com.sunrisekcdeveloper.design.ui.PrimaryButton
import com.sunrisekcdeveloper.home.PlantTabFilter.FORGOT_TO_WATER
import com.sunrisekcdeveloper.home.PlantTabFilter.HISTORY
import com.sunrisekcdeveloper.home.PlantTabFilter.UPCOMING
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper

@Composable
fun PlantListUiNew(
    viewModel: PlantListViewModel,
) {

    val selectedFilter by viewModel.filter.collectAsState()
    val plants by viewModel.plants.collectAsState()

    Column {
        FilterBar(selectedFilter = selectedFilter, onSelection = viewModel::onFilterChange)
        if (plants.isEmpty()) {
            EmptyList(viewModel::onAddPlantClick)
        }
    }
}

@Composable
private fun FilterBar(
    selectedFilter: PlantTabFilter,
    onSelection: (PlantTabFilter) -> Unit,
    modifier: Modifier = Modifier
) {

    @Composable
    fun FilterBarItem(
        text: String,
        selected: Boolean,
        onClick: () -> Unit,
    ) {
        Text(
            text = text,
            color = if (selected) accent500 else neutralus300,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.clickable { onClick() }
        )
    }

    Row(
        modifier = modifier
            .wrapContentWidth()
            .padding(start = 20.dp),
    ) {
        FilterBarItem(
            text = "Upcoming",
            selected = selectedFilter == UPCOMING,
            onClick = { onSelection(UPCOMING) }
        )
        Spacer(modifier = Modifier.weight(0.2f))

        FilterBarItem(
            text = "Forgot to Water",
            selected = selectedFilter == FORGOT_TO_WATER,
            onClick = { onSelection(FORGOT_TO_WATER) }
        )
        Spacer(modifier = Modifier.weight(0.2f))

        FilterBarItem(
            text = "History",
            selected = selectedFilter == HISTORY,
            onClick = { onSelection(HISTORY) }
        )
        Spacer(modifier = Modifier.weight(0.6f))
    }
}

@Composable
private fun EmptyList(
    onButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.three_plants),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.weight(0.1f))

        // todo string resources
        Text(
            text = "Sorry.",
            style = MaterialTheme.typography.displayLarge,
        )
        Spacer(modifier = Modifier.weight(0.05f))

        Text(
            text = "There are no plants in the list, please add your first plant.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 30.dp)
        )
        Spacer(modifier = Modifier.weight(0.05f))

        PrimaryButton(label = "Add Your First Plant", onButtonClick = { onButtonClick() }, )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun PlantListUi_Preview() {
    ThemeSurfaceWrapper {
        PlantListUiNew(PlantListViewModel.Fake())
    }
}