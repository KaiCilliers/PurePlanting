package com.sunrisekcdeveloper.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.sunrisekcdeveloper.plant.domain.Plant
import com.sunrisekcdeveloper.ui.TabFilterOption
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PlantListUi(
    viewModel: PlantListViewModel,
    modifier: Modifier = Modifier
) {

    val plants = viewModel.plants.collectAsState()
    val filterTabSelected = viewModel.filter.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showDeleteConfirmationDialog: Plant? by remember { mutableStateOf(null) }

    ObserveAsEvents(flow = viewModel.eventsFlow) { event ->
        when (event) {
            PlantListViewModel.Event.DeletedPlant -> {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = "Successfully deleted plant!",
                        actionLabel = "Undo"
                    )
                    
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            viewModel.onUndoDelete()
                        }
                        SnackbarResult.Dismissed -> Unit
                    }
                }
            }

            PlantListViewModel.Event.EditedPlant -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Successfully edited plant!"
                    )
                }
            }
        }
    }

    val plantToDelete = showDeleteConfirmationDialog
    if (plantToDelete != null) {
        AlertDialog(
            icon = {
                Icon(Icons.Filled.Delete, contentDescription = "Example Icon")
            },
            title = {
                Row {
                    Icon(
                        Icons.Filled.RemoveCircleOutline,
                        contentDescription = "Example Icon",
                        tint = Color.Red
                    )
                    Text(text = "Are you sure?", modifier.weight(1f))
                }
            },
            text = {
                Text(text = "Do you really want to delete the \"${plantToDelete.details.name}\"?")
            },
            onDismissRequest = { showDeleteConfirmationDialog = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onDeletePlant(plantToDelete)
                        showDeleteConfirmationDialog = null
                    }
                ) {
                    Text("Got it")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmationDialog = null }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues)
        ) {
            Text(
                text = "Build Type: todo",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
            Row(modifier) {
                PlantTabFilter.values().forEach {
                    TabFilterOption(
                        text = it.name,
                        isSelected = filterTabSelected.value == it,
                        modifier = Modifier.clickable { viewModel.onFilterChange(it) }
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "add",
                    modifier = Modifier
                        .padding(20.dp)
                        .size(40.dp)
                        .clickable {
                            viewModel.onAddPlantClick()
                        }
                )
            }

            if (plants.value.isNotEmpty()) {
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    items(plants.value) { plant ->
                        PlantCard(
                            plant = plant,
                            onWaterClicked = { viewModel.onWaterPlant(plant) },
                            onCardClick = { viewModel.onPlantClick(plant) },
                            onUndoWaterClicked = { viewModel.onUndoWater(plant) },
                            onDeletePlant = { showDeleteConfirmationDialog = plant }
                        )
                    }
                }
            } else {
                Text(text = "There are no plants in the list, try adding some!")
            }
        }
    }
}

@Composable
private fun <T> ObserveAsEvents(flow: Flow<T>, onEvent: (T) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(flow, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
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