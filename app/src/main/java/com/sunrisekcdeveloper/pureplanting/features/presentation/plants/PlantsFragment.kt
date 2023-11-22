package com.sunrisekcdeveloper.pureplanting.features.presentation.plants

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.work.WorkManager
import com.sunrisekcdeveloper.pureplanting.navigation.ComposeFragment
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestackextensions.servicesktx.lookup

class PlantsFragment : ComposeFragment() {
    @Composable
    override fun FragmentComposable(backstack: Backstack) {
        val viewModel = remember { backstack.lookup<PlantsViewModel>() }

        val activeFilter = viewModel.activeFilter.collectAsState()
        val plants = viewModel.plants.collectAsState()
        val plantNotificationState = WorkManager.getInstance(requireContext()).getWorkInfosForUniqueWorkLiveData("dailyWateringNotifications").observeAsState()

        PlantsScreen(
            plantsFilterOption = PlantListFilter.values().toList(),
            selectedFilter = activeFilter,
            onFilterSelected = { viewModel.setFilter(it) },
            plants = plants,
            onPlantTapped = viewModel::navigateToPlantDetail,
            onNotificationIconTapped = viewModel::navigateToNotification,
            onAddIconTapped = viewModel::navigateToAddPlant,
            waterWorkerState = plantNotificationState
        )
    }
}