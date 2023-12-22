package com.sunrisekcdeveloper.addEdit

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sunrisekcdeveloper.addEdit.ui.InputText
import com.sunrisekcdeveloper.addEdit.ui.WateringDaySelectionDialog
import com.sunrisekcdeveloper.addEdit.ui.PlantSizeSelectionDialog
import com.sunrisekcdeveloper.addEdit.ui.PPTimePickerDialog
import com.sunrisekcdeveloper.components.addEdit.R
import com.sunrisekcdeveloper.design.noRippleClickable
import com.sunrisekcdeveloper.design.theme.neutralus0
import com.sunrisekcdeveloper.design.theme.neutralus100
import com.sunrisekcdeveloper.design.theme.otherOlive500
import com.sunrisekcdeveloper.design.ui.BackIcon
import com.sunrisekcdeveloper.design.ui.PrimaryButton
import com.sunrisekcdeveloper.design.ui.PrimarySmallButton
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.sunrisekcdeveloper.library.design.R as designR

@Composable
fun AddEditUiNew(viewModel: AddEditViewModel) {

    val imgSrc: String by viewModel.image.collectImmediatelyAsState()

    PlantBox {
        BackIcon(
            onClick = {},
            modifier = Modifier.padding(top = 30.dp, start = 20.dp) // todo extract modifier to be used across screens
        )
        Column {
            Header(imgSrc)
            Spacer(modifier = Modifier.height(16.dp))
            InputSheet(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun InputSheet(
    viewModel: AddEditViewModel,
    modifier: Modifier = Modifier
) {

    val name by viewModel.name.collectImmediatelyAsState()
    val wateringDays by viewModel.wateringDays.collectImmediatelyAsState()
    val time by viewModel.wateringTime.collectImmediatelyAsState()
    val waterAmount by viewModel.wateringAmount.collectImmediatelyAsState()
    val size by viewModel.size.collectImmediatelyAsState()
    val description by viewModel.description.collectImmediatelyAsState()

    val configuration = LocalConfiguration.current
    var showingTimeTouchInput by remember { mutableStateOf(false) }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }

    var showSizeDialog by remember { mutableStateOf(false) }
    var showDaysDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }

    if (showSizeDialog) {
        PlantSizeSelectionDialog(
            onDismiss = { showSizeDialog = false },
            initialSelection = size,
            onSelection = viewModel::onSizeChanged
        )
    }

    if (showDaysDialog) {
        WateringDaySelectionDialog(
            onDismiss = { showDaysDialog = false },
            initialSelection = wateringDays,
            onSelection = viewModel::onWateringDaysChanged
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

    Column {
        Surface(
            color = neutralus0,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            modifier = modifier.weight(1f),
        ) {
            // prevent overscroll, see https://stackoverflow.com/questions/69468212/remove-lazycolumn-overscroll-effect-in-jetpack-compose
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BasicInput(
                        label = "Plant name*",
                        value = name,
                        onValueChange = viewModel::onNameChanged,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.height(IntrinsicSize.Min)
                    ) {
                        DialogInput(
                            label = "Dates",
                            value = when (wateringDays.size) {
                                1 -> {
                                    wateringDays.first().name.lowercase()
                                        .replaceFirstChar { it.titlecase(java.util.Locale.getDefault()) }
                                }
                                7 -> {
                                    "Everyday"
                                }
                                else -> {
                                    wateringDays.joinToString {
                                        it.name
                                            .take(3)
                                            .lowercase()
                                            .replaceFirstChar { it.titlecase(java.util.Locale.getDefault()) }
                                    }
                                }
                            },
                            onClick = { showDaysDialog = true },
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        DialogInput(
                            label = "Time",
                            value = time.format(timeFormatter),
                            onClick = { showTimeDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.height(IntrinsicSize.Min)
                    ) {
                        BasicInput(
                            label = "The amount of water*",
                            value = waterAmount,
                            onValueChange = viewModel::onWateringAmountChanged,
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        DialogInput(
                            label = "Plant Size*",
                            value = stringResource(id = size.textResId),
                            onClick = { showSizeDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    BasicInput(
                        label = "Description",
                        value = description,
                        onValueChange = viewModel::onDescriptionChanged,
                        singleLine = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    )
                }
            }
        }
        Surface(
            color = neutralus0
        ) {
            PrimaryButton(
                onClick = { viewModel.onSavePlant() },
                label = "Create a Plant",
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
            )
        }
    }
}


@Composable
private fun BasicInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(text = label)
        Spacer(modifier = Modifier.height(8.dp))
        InputText(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            maxLines = if (singleLine) 1 else Int.MAX_VALUE
        )
    }
}

@Composable
private fun DialogInput(
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(text = label)
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier
                .noRippleClickable { onClick() }
                .fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
            color = neutralus100
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(14.dp)
            ) {
                Text(
                    text = value,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(id = R.drawable.chevron_down),
                    contentDescription = "",
                    alignment = Alignment.CenterEnd,
                    modifier = Modifier,
                )
            }
        }
    }
}

// todo move all these private composables to internal ones into separate files
@Composable
fun Header(
    imgSrc: String? = null
) {
    if (imgSrc.isNullOrBlank()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = rememberAsyncImagePainter(model = Uri.parse(imgSrc)),
            contentDescription = "",
            alignment = Alignment.Center,
            contentScale = ContentScale.FillBounds
        )
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                PlantPlaceholderImage()
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            PrimarySmallButton(
                onClick = { /*TODO*/ },
                label = "Add Image"
            )
        }
    }
}

@Composable
private fun PlantPlaceholderImage(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(id = designR.drawable.single_plant_placeholder),
        contentDescription = "",
    )
}

@Composable
private fun PlantBox(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        contentAlignment = contentAlignment,
        modifier = modifier.background(otherOlive500.copy(alpha = 0.7f)) // todo uniform background color for screens, ie MaterialTheme :)
    ) {
        Image(
            painter = painterResource(id = designR.drawable.banner_plant),
            contentDescription = ""
        )
        content()
    }
}

@Preview
@Composable
fun AddEditUiNew_Preview() {
    ThemeSurfaceWrapper {
        AddEditUiNew(AddEditViewModel.Fake())
    }
}