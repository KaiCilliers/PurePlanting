package com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant.components.PlantSize
import com.sunrisekcdeveloper.pureplanting.navigation.ComposeFragment
import com.sunrisekcdeveloper.pureplanting.navigation.ThemeSurfaceWrapper
import com.sunrisekcdeveloper.pureplanting.util.CameraPermissionTextProvider
import com.sunrisekcdeveloper.pureplanting.util.PermissionDialog
import com.sunrisekcdeveloper.pureplanting.util.ReadMediaImagesPermissionTextProvider
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

            val dialogQueue = viewModel.visiblePermissionDialogQueue

            // TODO: move permissions to button tap for camera of image picker
            val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { perms ->
                    permissionsToRequest.forEach { permission ->
                        viewModel.onPermissionResult(
                            permission = permission,
                            isGranted = perms[permission] == true
                        )
                    }
                }
            )

            LaunchedEffect(key1 = Unit) {
                multiplePermissionResultLauncher.launch(permissionsToRequest.toTypedArray())
            }

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
                    onAddPlantTap = {
                        viewModel.savePlant()
                        backstack.jumpToRoot()
                    },
                    modifier = Modifier.fillMaxSize()
                )

                dialogQueue
                    .reversed()
                    .forEach { permission ->
                        PermissionDialog(
                            permissionTextProvider = when (permission) {
                                Manifest.permission.CAMERA -> {
                                    CameraPermissionTextProvider()
                                }
                                Manifest.permission.READ_MEDIA_IMAGES -> {
                                    ReadMediaImagesPermissionTextProvider()
                                }
                                else -> return@forEach
                            },
                            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                permission
                            ),
                            onDismiss = viewModel::dismissDialog,
                            onOkClick = {
                                viewModel.dismissDialog()
                                multiplePermissionResultLauncher.launch(
                                    arrayOf(permission)
                                )
                            },
                            onGoToAppSettingsClick = ::openAppSettings
                        )
                    }
            }
        }
    }
}

fun Fragment.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", requireActivity().packageName, null)
    ).also(::startActivity)
}