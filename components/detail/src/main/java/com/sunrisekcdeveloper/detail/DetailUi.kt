@file:Suppress("SameParameterValue")

package com.sunrisekcdeveloper.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.design.noRippleClickable
import com.sunrisekcdeveloper.design.topEndIconPadding
import com.sunrisekcdeveloper.design.topStartIconPadding
import com.sunrisekcdeveloper.design.ui.BackIcon
import com.sunrisekcdeveloper.detail.models.DetailItem
import com.sunrisekcdeveloper.detail.ui.BannerDetails
import com.sunrisekcdeveloper.detail.ui.DetailSheet
import com.sunrisekcdeveloper.detail.ui.EditIcon
import com.sunrisekcdeveloper.detail.ui.Header
import com.sunrisekcdeveloper.detail.ui.PlantBox
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper
import java.time.LocalDateTime

@Composable
fun DetailUi(viewModel: DetailViewModel) {

    val plant by viewModel.plant.collectAsState()
    val needsWaterToday = plant.needsWaterToday(LocalDateTime.now())

    PlantBox {
        Header(
            plant.details.imageSrcUri
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxHeight(0.7f)
        ) {
            BannerDetails(
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .padding(bottom = 40.dp),
                labels = listOf(
                    // todo string resource
                    DetailItem(label = "Size", value = plant.details.size),
                    DetailItem(label = "Water", value = plant.wateringInfo.amount),
                    // todo string resource
                    DetailItem(label = "Frequency", value = "${plant.wateringInfo.days.size} times/week"),
                ),
            )
            DetailSheet(
                title = plant.details.name,
                description = plant.details.description,
                onButtonClick = { viewModel.onWaterPlant() },
                needsWaterToday = needsWaterToday
            )
        }
        BackIcon(
            onClick = viewModel::onGoBack,
            modifier = Modifier.topStartIconPadding()
        )
        EditIcon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .topEndIconPadding()
                .noRippleClickable { viewModel.onEditPlant() }
        )
    }
}

@Preview
@Composable
private fun DetailUi_Preview() {
    ThemeSurfaceWrapper {
        DetailUi(DetailViewModel.Fake())
    }
}