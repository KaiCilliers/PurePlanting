package com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sunrisekcdeveloper.pureplanting.features.presentation.plantdetail.LargeText
import com.sunrisekcdeveloper.pureplanting.features.presentation.plantdetail.SimpleSetupPreview
import com.sunrisekcdeveloper.pureplanting.navigation.ComposeFragment
import com.zhuinden.simplestack.Backstack

class AddEditPlantFragment : ComposeFragment() {
    @Composable
    override fun FragmentComposable(backstack: Backstack) {
        SimpleSetupPreview {
            LargeText(text = "Add Edit Screen")
            LargeText(text = "Go Back", modifier = Modifier.clickable { backstack.goBack() })
        }
    }
}