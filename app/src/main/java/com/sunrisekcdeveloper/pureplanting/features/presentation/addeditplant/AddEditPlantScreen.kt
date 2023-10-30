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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.sunrisekcdeveloper.pureplanting.features.component.plants.Plant
import com.sunrisekcdeveloper.pureplanting.navigation.ThemeSurfaceWrapper
import java.io.File
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.Date

@Composable
fun AddEditPlantScreen(
    name: String,
    nameUpdater: (String) -> Unit,
    description: String,
    descriptionUpdater: (String) -> Unit,
    size: String,
    sizeUpdater: (String) -> Unit,
    daysToWater: List<DayOfWeek>,
    daysToWaterUpdater: (String) -> Unit,
    wateringHour: Int,
    wateringHourUpdater: (String) -> Unit,
    amountOfWater: String,
    amountOfWaterUpdater: (String) -> Unit,
    imgSrcUri: String,
    imgSrcUriUpdater: (String) -> Unit,
    onAddPlantTap: (Plant) -> Unit,
    modifier: Modifier = Modifier
) {

    var uri: Uri? = null

    var capturedImageUri by remember {
        mutableStateOf(imgSrcUri.toUri())
    }

    val context = LocalContext.current

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
                    .clickable {
                        galleryImagePickerLauncher.launch("image/*")
                    })
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

        Spacer(modifier = Modifier.height(12.dp))
        LabelAndPlaceHolderTextField(text = name, onValueChanged = nameUpdater)

        Spacer(modifier = Modifier.height(12.dp))
        LabelAndPlaceHolderTextField(text = description, onValueChanged = descriptionUpdater)

        Spacer(modifier = Modifier.height(12.dp))
        LabelAndPlaceHolderTextField(text = size, onValueChanged = sizeUpdater)

        Spacer(modifier = Modifier.height(12.dp))
        LabelAndPlaceHolderTextField(text = daysToWater.toString(), onValueChanged = daysToWaterUpdater)

        Spacer(modifier = Modifier.height(12.dp))
        LabelAndPlaceHolderTextField(text = wateringHour.toString(), onValueChanged = wateringHourUpdater)

        Spacer(modifier = Modifier.height(12.dp))
        LabelAndPlaceHolderTextField(text = amountOfWater, onValueChanged = amountOfWaterUpdater)

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            val newPlant = Plant.createNewPlant(
                imageSrc = imgSrcUri,
                name = name,
                description = description,
                size = size,
                wateringDays = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
                wateringHour = wateringHour,
                wateringAmount = amountOfWater,
            )
            onAddPlantTap(newPlant)
        }) {
            Text(text = "Create a Plant")
        }
    }
}

fun Context.createTempFileUri(): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelAndPlaceHolderTextField(
    text: String,
    onValueChanged: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = onValueChanged,
        label = { Text(text = "Your Label") },
        placeholder = { Text(text = "Your Placeholder/Hint") },
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
            size = "dolore",
            sizeUpdater = {},
            daysToWater = listOf(),
            daysToWaterUpdater = {},
            wateringHour = 9647,
            wateringHourUpdater = {},
            amountOfWater = "appetere",
            amountOfWaterUpdater = {},
            imgSrcUri = "expetendis",
            imgSrcUriUpdater = {},
            onAddPlantTap = {}
        )
    }
}