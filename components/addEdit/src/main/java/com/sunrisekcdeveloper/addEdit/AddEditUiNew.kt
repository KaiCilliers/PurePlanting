package com.sunrisekcdeveloper.addEdit

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sunrisekcdeveloper.addEdit.ui.InputText
import com.sunrisekcdeveloper.design.theme.neutralus100
import com.sunrisekcdeveloper.design.theme.otherOlive500
import com.sunrisekcdeveloper.design.ui.BackIcon
import com.sunrisekcdeveloper.design.ui.PrimarySmallButton
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper
import com.sunrisekcdeveloper.library.design.R as designR

@Composable
fun AddEditUiNew(viewModel: AddEditViewModel) {

    val imgSrc: String by viewModel.image.collectImmediatelyAsState()

    PlantBox {
        BackIcon(onClick = {})
        Column {
            Header(imgSrc)
            InputSheet(viewModel)
        }
    }
}

@Composable
private fun InputSheet(
    viewModel: AddEditViewModel,
    modifier: Modifier = Modifier
) {

    val name by viewModel.name.collectImmediatelyAsState()
    val dates by viewModel.wateringDays.collectImmediatelyAsState()
    val time by viewModel.wateringTime.collectImmediatelyAsState()
    val waterAmount by viewModel.wateringAmount.collectImmediatelyAsState()
    val size by viewModel.size.collectImmediatelyAsState()
    val description by viewModel.description.collectImmediatelyAsState()

    Surface(
        color = neutralus100,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                DialogInput(
                    label = "Dates",
                    value = name,
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f)
                )
                DialogInput(
                    label = "Time",
                    value = name,
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicInput(
                    label = "The amount of water*",
                    value = waterAmount,
                    onValueChange = viewModel::onWateringAmountChanged,
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                DialogInput(
                    label = "Plant Size*",
                    value = name,
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            BasicInput(
                label = "Description",
                value = description,
                onValueChange = viewModel::onDescriptionChanged,
                singleLine = false,
                modifier = Modifier.fillMaxWidth()
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
        InputText(
            value = value,
            onValueChange = {},
            modifier = Modifier
                .clickable { onClick() }
                .fillMaxWidth()
        )
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                PlantPlaceholderImage()
            }
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