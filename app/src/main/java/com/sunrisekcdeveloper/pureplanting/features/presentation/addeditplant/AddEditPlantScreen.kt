@file:OptIn(ExperimentalMaterial3Api::class)

package com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
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
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant.components.PPDateSelectionDialog
import com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant.components.PPSizeSelectionDialog
import com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant.components.PPTextFieldReadOnly
import com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant.components.PPTimePickerDialog
import com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant.components.PlantSize
import com.sunrisekcdeveloper.pureplanting.navigation.ThemeSurfaceWrapper
import java.io.File
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPlantScreen(
    name: String,
    nameUpdater: (String) -> Unit,
    description: String,
    descriptionUpdater: (String) -> Unit,
    size: PlantSize,
    sizeUpdater: (String) -> Unit,
    daysToWater: List<DayOfWeek>,
    daysToWaterUpdater: (List<DayOfWeek>) -> Unit,
    wateringTime: LocalTime,
    wateringTimeUpdater: (LocalTime) -> Unit,
    amountOfWater: String,
    amountOfWaterUpdater: (String) -> Unit,
    imgSrcUri: String,
    imgSrcUriUpdater: (String) -> Unit,
    onAddPlantTap: () -> Unit,
    modifier: Modifier = Modifier
) {

    var uri: Uri? = null
    var capturedImageUri by remember { mutableStateOf(imgSrcUri.toUri()) }
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful ->
        if (isSuccessful) {
            uri?.let {
                imgSrcUriUpdater(it.toString())
                capturedImageUri = it
            }
        } else {
            uri = null
        }
    }

    val galleryImagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { imgUri ->
        capturedImageUri = imgUri ?: return@rememberLauncherForActivityResult
        imgSrcUriUpdater(imgUri.toString())
    }

    var showSizeDialog by remember { mutableStateOf(false) }
    var showDatesDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }

    var selectedPlantSize: PlantSize by remember { mutableStateOf(size) }
    var selectedWateringDates: List<DayOfWeek> by remember { mutableStateOf(daysToWater) }
    var selectedTime: LocalTime by remember { mutableStateOf(wateringTime) }
    var showingTimeTouchInput by remember { mutableStateOf(false) }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }

    // todo animate dialog visibility
    if (showSizeDialog) {
        PPSizeSelectionDialog(
            dismiss = { showSizeDialog = false },
            initialSelection = selectedPlantSize,
            updateSelection = {
                selectedPlantSize = it
                sizeUpdater(it.name)
            }
        )
    }

    if (showDatesDialog) {
        PPDateSelectionDialog(
            dismiss = { showDatesDialog = false },
            initialSelections = selectedWateringDates,
            updateSelection = {
                selectedWateringDates = it
                daysToWaterUpdater(it)
            }
        )
    }

    if (showTimeDialog) {
        val timeDialogState = rememberTimePickerState()
        PPTimePickerDialog(
            onCancel = { showTimeDialog = false },
            onConfirm = {
                selectedTime = LocalTime.of(timeDialogState.hour, timeDialogState.minute, 0, 0)
                wateringTimeUpdater(selectedTime)
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
                    // todo place this someplace else
                    TimePicker(state = timeDialogState)
                } else {
                    TimeInput(state = timeDialogState)
                }
            }
        )
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
                        uri = context.createTempFileUri()
                        cameraLauncher.launch(uri)
                    })
            Spacer(modifier = Modifier.width(100.dp))
            Icon(imageVector = Icons.Filled.Image, contentDescription = "",
                modifier = Modifier
                    .size(42.dp)
                    .clickable { galleryImagePickerLauncher.launch("image/*") }
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
            text = stringResource(id = selectedPlantSize.textResId),
            onClick = { showSizeDialog = true }
        )
        Spacer(modifier = Modifier.height(12.dp))

        PPTextFieldReadOnly(
            text = selectedWateringDates.joinToString(", ") { it.name.substring(0, 3) },
            onClick = { showDatesDialog = true }
        )
        Spacer(modifier = Modifier.height(12.dp))

        PPTextFieldReadOnly(
            text = selectedTime.format(timeFormatter),
            onClick = { showTimeDialog = true }
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = name,
            onValueChange = nameUpdater,
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = description,
            onValueChange = descriptionUpdater,
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = amountOfWater,
            onValueChange = amountOfWaterUpdater,
            suffix = { Text(" ml") }
        )
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onAddPlantTap() }
        ) {
            Text(text = "Create a Plant")
        }
    }
}

// TODO: worker to clear temp files
private fun Context.createTempFileUri(): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "PNG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".png", /* suffix */
        filesDir
    )
    return FileProvider.getUriForFile(
        this,
        "com.sunrisekcdeveloper.pureplanting" + ".provider", image
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun AddEditPlantScreen_Preview() {
    ThemeSurfaceWrapper {
        AddEditPlantScreen(
            name = "Pamela Salazar",
            nameUpdater = {},
            description = "posidonium",
            descriptionUpdater = {},
            size = PlantSize.Large,
            sizeUpdater = {},
            daysToWater = listOf(DayOfWeek.MONDAY),
            daysToWaterUpdater = {},
            wateringTime = LocalTime.of(1, 1),
            wateringTimeUpdater = {},
            amountOfWater = "appetere",
            amountOfWaterUpdater = {},
            imgSrcUri = "expetendis",
            imgSrcUriUpdater = {},
            onAddPlantTap = {},
        )
    }
}