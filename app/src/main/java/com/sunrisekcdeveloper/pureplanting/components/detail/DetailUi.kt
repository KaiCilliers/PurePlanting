@file:Suppress("SameParameterValue")

package com.sunrisekcdeveloper.pureplanting.components.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.library.design.noRippleClickable
import com.sunrisekcdeveloper.pureplanting.library.design.topEndIconPadding
import com.sunrisekcdeveloper.pureplanting.library.design.topStartIconPadding
import com.sunrisekcdeveloper.pureplanting.library.design.ui.BackIcon
import com.sunrisekcdeveloper.pureplanting.library.design.ui.PlantBox
import com.sunrisekcdeveloper.pureplanting.components.detail.models.DetailItem
import com.sunrisekcdeveloper.pureplanting.components.detail.ui.BannerDetails
import com.sunrisekcdeveloper.pureplanting.components.detail.ui.DetailSheet
import com.sunrisekcdeveloper.pureplanting.components.detail.ui.EditIcon
import com.sunrisekcdeveloper.pureplanting.components.detail.ui.Header
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper
import java.time.LocalDateTime

@Composable
fun DetailUi(viewModel: DetailViewModel) {

    val plant by viewModel.plant.collectAsState()
    val needsWaterToday = plant.waterTimeIsUpcoming(LocalDateTime.now())

    PlantBox {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Header(plant.details.imageSrcUri)
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
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .topStartIconPadding()
            )
            EditIcon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .topEndIconPadding()
                    .noRippleClickable { viewModel.onEditPlant() }
            )
        }
    }
}

@Preview
@Composable
private fun DetailUi_Preview() {
    ThemeSurfaceWrapper {
        DetailUi(DetailViewModel.Fake())
    }
}