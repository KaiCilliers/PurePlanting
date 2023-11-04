package com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant.components.PlantSize
import com.sunrisekcdeveloper.pureplanting.navigation.ComposeFragment
import com.sunrisekcdeveloper.pureplanting.navigation.ThemeSurfaceWrapper
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestackextensions.servicesktx.lookup
import kotlinx.coroutines.flow.update

class AddEditPlantFragment : ComposeFragment() {

    private val permissionsToRequest = arrayListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionsToRequest.add(Manifest.permission.CAMERA)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
        }
    }

    @Composable
    override fun FragmentComposable(backstack: Backstack) {
        ThemeSurfaceWrapper {
            val viewModel = remember { backstack.lookup<AddEditPlantViewModel>() }

            val name by viewModel.name.collectAsState()
            val description by viewModel.description.collectAsState()
            val imgSrcUri by viewModel.image.collectAsState()
            val size by viewModel.size.collectAsState()
            val wateringDays by viewModel.wateringDays.collectAsState()
            val wateringTime by viewModel.wateringTime.collectAsState()
            val wateringAmount by viewModel.wateringAmount.collectAsState()

            Box {
                AddEditPlantScreen(
                    name = name,
                    nameUpdater = { newValue -> viewModel.name.update { newValue } },
                    description = description,
                    descriptionUpdater = { newValue -> viewModel.description.update { newValue } },
                    size = size,
                    sizeUpdater = { newValue -> viewModel.size.update { PlantSize.valueOf(newValue) } },
                    imgSrcUri = imgSrcUri,
                    imgSrcUriUpdater = { newValue -> viewModel.image.update { newValue } },
                    daysToWater = wateringDays,
                    daysToWaterUpdater = { newValue -> viewModel.wateringDays.update { newValue } },
                    wateringTime = wateringTime,
                    wateringTimeUpdater = { newValue -> viewModel.wateringTime.update { newValue } },
                    amountOfWater = wateringAmount,
                    amountOfWaterUpdater = { newValue -> viewModel.wateringAmount.update { newValue } },
                    onAddPlantTap = { viewModel.savePlant() },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}