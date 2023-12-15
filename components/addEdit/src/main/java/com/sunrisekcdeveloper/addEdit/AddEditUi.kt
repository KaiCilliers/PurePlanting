package com.sunrisekcdeveloper.addEdit

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.sunrisekcdeveloper.addEdit.ui.PPDateSelectionDialog
import com.sunrisekcdeveloper.addEdit.ui.PPSizeSelectionDialog
import com.sunrisekcdeveloper.addEdit.ui.PPTextFieldReadOnly
import com.sunrisekcdeveloper.addEdit.ui.PPTimePickerDialog
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditUi(
    viewModel: AddEditViewModel,
    modifier: Modifier = Modifier,
) {

    val image by viewModel.image.collectImmediatelyAsState()
    val name by viewModel.name.collectImmediatelyAsState()
    val description by viewModel.description.collectImmediatelyAsState()
    val size by viewModel.size.collectImmediatelyAsState()
    val wateringDays by viewModel.wateringDays.collectImmediatelyAsState()
    val wateringTime by viewModel.wateringTime.collectImmediatelyAsState()
    val wateringAmount by viewModel.wateringAmount.collectImmediatelyAsState()

    var uri: Uri? = null
    var capturedImageUri by remember { mutableStateOf(image.toUri()) }
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    var showSizeDialog by remember { mutableStateOf(false) }
    var showDatesDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }

    var showingTimeTouchInput by remember { mutableStateOf(false) }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }

    var showCameraPermissionRationale by remember { mutableStateOf(false) }
    var hasAskedCameraPermissionBefore by remember { mutableStateOf(false) }
    var showGalleryPermissionRationale by remember { mutableStateOf(false) }
    var hasAskedGalleryPermissionBefore by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful ->
        if (isSuccessful) {
            uri?.let {
                viewModel.onImageChanged(it.toString())
                capturedImageUri = it
            }
        } else {
            uri = null
        }
    }


    val galleryImagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { imgUri ->
        capturedImageUri = imgUri ?: return@rememberLauncherForActivityResult
        viewModel.onImageChanged(imgUri.toString())
    }

    val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasAskedCameraPermissionBefore = true
            if (isGranted) {
                uri = context.createTempFileUri()
                cameraLauncher.launch(uri)
            }
        }
    )

    val galleryPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasAskedGalleryPermissionBefore = true
            if (isGranted) {
                galleryImagePickerLauncher.launch("image/*")
            }
        }
    )

    if (showSizeDialog) {
        PPSizeSelectionDialog(
            dismiss = { showSizeDialog = false },
            initialSelection = size,
            updateSelection = viewModel::onSizeChanged
        )
    }

    if (showDatesDialog) {
        PPDateSelectionDialog(
            dismiss = { showDatesDialog = false },
            initialSelections = wateringDays,
            updateSelection = viewModel::onWateringDaysChanged
        )
    }

    if (showTimeDialog) {
        val timeDialogState = rememberTimePickerState()
        PPTimePickerDialog(
            onCancel = { showTimeDialog = false },
            onConfirm = {
                viewModel.onWateringTimeChanged(LocalTime.of(timeDialogState.hour, timeDialogState.minute, 0, 0))
                showTimeDialog = false
            },
            toggle = {
                if (configuration.screenHeightDp > 400) {
                    IconButton(onClick = { showingTimeTouchInput = !showingTimeTouchInput }) {
                        val icon = if (showingTimeTouchInput) {
                            Icons.Outlined.Keyboard
                        } else {
                            Icons.Outlined.Schedule
                        }
                        Icon(
                            icon,
                            contentDescription = if (showingTimeTouchInput) {
                                "Switch to Text Input"
                            } else {
                                "Switch to Touch Input"
                            }
                        )
                    }
                }
            },
            content = {
                if (showingTimeTouchInput && configuration.screenHeightDp > 400) {
                    TimePicker(state = timeDialogState)
                } else {
                    TimeInput(state = timeDialogState)
                }
            }
        )
    }

    if (showCameraPermissionRationale) {
        PermissionDialog(
            permissionTextProvider = CameraPermissionTextProvider(),
            isPermanentlyDeclined = !context.shouldShowRationale(Manifest.permission.CAMERA) && hasAskedCameraPermissionBefore,
            onDismiss = { showCameraPermissionRationale = false },
            onOkClick = { cameraPermissionResultLauncher.launch(Manifest.permission.CAMERA) },
            onGoToAppSettingsClick = { context.openAppSettings() })
    }

    if (showGalleryPermissionRationale) {
        PermissionDialog(
            permissionTextProvider = ReadMediaImagesPermissionTextProvider(),
            isPermanentlyDeclined = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                !context.shouldShowRationale(Manifest.permission.READ_MEDIA_IMAGES) && hasAskedGalleryPermissionBefore
            } else false,
            onDismiss = { showGalleryPermissionRationale = false },
            onOkClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    galleryPermissionResultLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            },
            onGoToAppSettingsClick = { context.openAppSettings() })
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {

        Row {
            Icon(imageVector = Icons.Filled.Camera, contentDescription = "",
                modifier = Modifier
                    .size(42.dp)
                    .clickable {
                        if (context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            uri = context.createTempFileUri()
                            cameraLauncher.launch(uri)
                        } else {
                            showCameraPermissionRationale = true
                        }
                    })
            Spacer(modifier = Modifier.width(100.dp))
            Icon(imageVector = Icons.Filled.Image, contentDescription = "",
                modifier = Modifier
                    .size(42.dp)
                    .clickable {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (context.checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                                galleryImagePickerLauncher.launch("image/*")
                            } else {
                                showGalleryPermissionRationale = true
                            }
                        } else {
                            galleryImagePickerLauncher.launch("image/*")
                        }
                    }
            )
        }

        if (capturedImageUri.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .padding(16.dp, 8.dp)
                    .size(320.dp),
                painter = rememberAsyncImagePainter(capturedImageUri),
                contentDescription = "",
            )
        }

        PPTextFieldReadOnly(
            text = stringResource(id = size.textResId),
            onClick = { showSizeDialog = true }
        )
        Spacer(modifier = Modifier.height(12.dp))

        PPTextFieldReadOnly(
            text = wateringDays.joinToString(", ") { it.name.substring(0, 3) },
            onClick = { showDatesDialog = true }
        )
        Spacer(modifier = Modifier.height(12.dp))

        PPTextFieldReadOnly(
            text = wateringTime.format(timeFormatter),
            onClick = { showTimeDialog = true }
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = name,
            onValueChange = viewModel::onNameChanged,
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = description,
            onValueChange = viewModel::onDescriptionChanged,
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = wateringAmount,
            onValueChange = viewModel::onWateringAmountChanged,
        )
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = viewModel::onSavePlant
        ) {
            Text(text = "Create a Plant")
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun AddEditUi_Preview() {
    ThemeSurfaceWrapper {
        AddEditUi(AddEditViewModel.Fake())
    }
}