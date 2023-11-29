package com.sunrisekcdeveloper.pureplanting.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.sunrisekcdeveloper.android.toParcelable
import com.sunrisekcdeveloper.navigation.ComposeFragment
import com.sunrisekcdeveloper.plantDetail.PlantDetailScreen
import com.sunrisekcdeveloper.plantDetail.PlantDetailsViewModel
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestackextensions.servicesktx.lookup

class PlantDetailFragment : ComposeFragment() {
    @Composable
    override fun FragmentComposable(backstack: Backstack) {
        val viewModel = remember { backstack.lookup<PlantDetailsViewModel>() }

        val plant = viewModel.activePlant.collectAsState()

        PlantDetailScreen(
            plant = plant,
            onWateredButtonTapped = { viewModel.waterPlant() },
            onEditPlantTapped = { backstack.goTo(AddEditPlantKey(plant.value.toParcelable())) }
        )
    }
}