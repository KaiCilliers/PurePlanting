package com.sunrisekcdeveloper.pureplanting.features

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.sunrisekcdeveloper.addEdit.AddEditUiNew
import com.sunrisekcdeveloper.addEdit.AddEditViewModel
import com.sunrisekcdeveloper.android.navigation.ComposeFragment
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestackextensions.servicesktx.lookup

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
            val viewModel = remember { backstack.lookup<AddEditViewModel>() }

            AddEditUiNew(viewModel)
        }
    }
}