package com.sunrisekcdeveloper.pureplanting.features.presentation.plants

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant.AddEditPlantKey
import com.sunrisekcdeveloper.pureplanting.features.presentation.notifications.NotificationsKey
import com.sunrisekcdeveloper.pureplanting.features.presentation.plantdetail.LargeText
import com.sunrisekcdeveloper.pureplanting.features.presentation.plantdetail.PlantDetailKey
import com.sunrisekcdeveloper.pureplanting.features.presentation.plantdetail.SimpleSetupPreview
import com.sunrisekcdeveloper.pureplanting.navigation.ComposeFragment
import com.zhuinden.simplestack.Backstack

class PlantsFragment : ComposeFragment() {
    @Composable
    override fun FragmentComposable(backstack: Backstack) {
        SimpleSetupPreview {
            LargeText(text = "Plants")
            LargeText(text = "Add Plant", modifier = Modifier.clickable { backstack.goTo(AddEditPlantKey) })
            LargeText(text = "Notifications", modifier = Modifier.clickable { backstack.goTo(NotificationsKey) })
            LargeText(text = "Plant Detail", modifier = Modifier.clickable { backstack.goTo(PlantDetailKey) })
        }
    }
}