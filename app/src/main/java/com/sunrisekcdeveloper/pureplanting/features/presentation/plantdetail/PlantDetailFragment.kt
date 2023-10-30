package com.sunrisekcdeveloper.pureplanting.features.presentation.plantdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant.AddEditPlantKey
import com.sunrisekcdeveloper.pureplanting.navigation.ComposeFragment
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestackextensions.servicesktx.lookup

class PlantDetailFragment : ComposeFragment() {
    @Composable
    override fun FragmentComposable(backstack: Backstack) {
        val viewModel = remember { backstack.lookup<PlantDetailsViewModel>() }

        val plant = viewModel.activePlant.collectAsState()

        PlantDetailScreen(
            plant = plant,
            onWateredButtonTapped = viewModel::waterPlant,
            onEditPlantTapped = { backstack.goTo(AddEditPlantKey(it)) }
        )
    }
}

// todo remove below
@Composable
fun LargeText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 42.sp,
        modifier = modifier,
    )
}

@Composable
fun SimpleSetupPreview(
    content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) { content() }
}