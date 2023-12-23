package com.sunrisekcdeveloper.home.subcomponents

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.components.home.R
import com.sunrisekcdeveloper.design.noRippleClickable
import com.sunrisekcdeveloper.design.theme.accent500
import com.sunrisekcdeveloper.design.theme.neutralus300
import com.sunrisekcdeveloper.design.ui.BoxWithBottomFade
import com.sunrisekcdeveloper.design.ui.PrimaryButton
import com.sunrisekcdeveloper.home.models.PlantTabFilter
import com.sunrisekcdeveloper.home.models.PlantTabFilter.FORGOT_TO_WATER
import com.sunrisekcdeveloper.home.models.PlantTabFilter.HISTORY
import com.sunrisekcdeveloper.home.models.PlantTabFilter.UPCOMING
import com.sunrisekcdeveloper.home.ui.DeleteConfirmationDialog
import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PlantListUi(viewModel: PlantListViewModel) {

    val selectedFilter by viewModel.filter.collectAsState()
    val plants by viewModel.plants.collectAsState()

    var requestToDelete: Plant? by remember { mutableStateOf(null) }
    Box {
        Column {
            FilterBar(selectedFilter = selectedFilter, onSelection = viewModel::onFilterChange)
            if (plants.isEmpty()) {
                EmptyList(viewModel::onAddPlantClick)
            } else {
                BoxWithBottomFade {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(plants, key = { it.id }) { plant ->
                            com.sunrisekcdeveloper.home.ui.PlantCard(
                                plant = plant,
                                needsWater = plant.needsWaterToday(LocalDateTime.now()),
                                onWaterToggleClick = { needsWater ->
                                    if (needsWater) {
                                        viewModel.onWaterPlant(plant)
                                    } else {
                                        viewModel.onUndoWater(plant)
                                    }
                                },
                                onDeletePlant = { requestToDelete = plant },
                                onCardClick = { viewModel.onPlantClick(plant) },
                                modifier = Modifier.animateItemPlacement()
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = viewModel::onAddPlantClick,
            containerColor = accent500, // todo use MaterialTheme color
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 60.dp)
                .padding(end = 20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "Add Plant",
                modifier = Modifier.scale(1.2f)
            )
        }
    }

    val plantRequestedToDelete = requestToDelete
    if (plantRequestedToDelete != null) {
        DeleteConfirmationDialog(
            plantName = plantRequestedToDelete.details.name,
            onConfirm = {
                viewModel.onDeletePlant(plantRequestedToDelete)
                requestToDelete = null
            },
            onDismiss = { requestToDelete = null }
        )
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
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Min)
        ) {
            Text(
                text = text,
                color = if (selected) accent500 else neutralus300,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .noRippleClickable { onClick() },
            )
            if (selected) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth(0.5f)
                        .clip(CircleShape)
                        .background(accent500)
                )
            }
        }
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
    BoxWithBottomFade {
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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

            PrimaryButton(label = "Add Your First Plant", onClick = { onButtonClick() })
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun PlantListUi_Preview() {
    ThemeSurfaceWrapper {
        PlantListUi(PlantListViewModel.Fake())
    }
}