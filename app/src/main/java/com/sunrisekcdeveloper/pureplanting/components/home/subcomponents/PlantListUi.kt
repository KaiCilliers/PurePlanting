package com.sunrisekcdeveloper.pureplanting.components.home.subcomponents

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.library.design.theme.accent500
import com.sunrisekcdeveloper.pureplanting.library.design.ui.BoxWithBottomFade
import com.sunrisekcdeveloper.pureplanting.business.plant.Plant
import com.sunrisekcdeveloper.pureplanting.R
import com.sunrisekcdeveloper.pureplanting.components.home.ui.DeleteConfirmationDialog
import com.sunrisekcdeveloper.pureplanting.components.home.ui.EmptyPlantList
import com.sunrisekcdeveloper.pureplanting.components.home.ui.FilterBar
import com.sunrisekcdeveloper.pureplanting.components.home.ui.PlantCard
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PlantListUi(
    viewModel: PlantListViewModel,
    modifier: Modifier = Modifier,
) {

    val selectedFilter by viewModel.filter.collectAsState()
    val plants by viewModel.plants.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var requestToDelete: Plant? by remember { mutableStateOf(null) }

    Box(modifier) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ){
            FilterBar(selectedFilter = selectedFilter, onSelection = viewModel::onFilterChange)
            when {
                isLoading -> {
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = accent500
                    )
                }

                plants.isEmpty() -> {
                    EmptyPlantList(selectedFilter = selectedFilter)
                }

                else -> {
                    BoxWithBottomFade {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            items(plants, key = { it.id }) { plant ->
                                PlantCard(
                                    plant = plant,
                                    needsWater = plant.needsWater(LocalDateTime.now()),
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
                            item {
                                Spacer(modifier = Modifier.height(100.dp))
                            }
                        }
                    }
                }
            }
        }

        if(!isLoading) {
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


@Preview
@Composable
private fun PlantListUi_Preview() {
    ThemeSurfaceWrapper {
        PlantListUi(PlantListViewModel.Fake())
    }
}