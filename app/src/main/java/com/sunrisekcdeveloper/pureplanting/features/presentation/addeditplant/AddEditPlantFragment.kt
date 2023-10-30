package com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.sunrisekcdeveloper.pureplanting.navigation.ComposeFragment
import com.sunrisekcdeveloper.pureplanting.navigation.ThemeSurfaceWrapper
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestackextensions.servicesktx.lookup
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek

class AddEditPlantFragment : ComposeFragment() {
    @Composable
    override fun FragmentComposable(backstack: Backstack) {
        ThemeSurfaceWrapper {
            val viewModel = remember { backstack.lookup<AddEditPlantViewModel>() }

            val name by viewModel.name.collectAsState()
            val description by viewModel.description.collectAsState()
            val imgSrcUri by viewModel.image.collectAsState()
            val size by viewModel.size.collectAsState()
            val wateringDays by viewModel.wateringDays.collectAsState()
            val wateringHour by viewModel.wateringHour.collectAsState()
            val wateringAmount by viewModel.wateringAmount.collectAsState()

            AddEditPlantScreen(
                name = name,
                nameUpdater = { newValue -> viewModel.name.update { newValue } },
                description = description,
                descriptionUpdater = { newValue -> viewModel.description.update { newValue } },
                size = size,
                sizeUpdater = { newValue -> viewModel.size.update { newValue } },
                imgSrcUri = imgSrcUri,
                imgSrcUriUpdater = { newValue -> viewModel.image.update { newValue } },
                daysToWater = wateringDays,
                daysToWaterUpdater = { newValue -> viewModel.wateringDays.update { listOf(DayOfWeek.FRIDAY) } },
                wateringHour = wateringHour,
                wateringHourUpdater = { newValue -> viewModel.wateringHour.update { newValue.toInt() } },
                amountOfWater = wateringAmount,
                amountOfWaterUpdater = { newValue -> viewModel.wateringAmount.update { newValue } },
                onAddPlantTap = {
                    viewModel.savePlant()
                    backstack.jumpToRoot()
                },
            )
        }
    }
}