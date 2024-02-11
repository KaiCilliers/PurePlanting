package com.sunrisekcdeveloper.pureplanting.components.detail.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.components.detail.models.DetailItem
import com.sunrisekcdeveloper.pureplanting.core.design.ui.ThemeSurfaceWrapper

@Composable
internal fun BannerDetails(
    labels: List<DetailItem>,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
        tonalElevation = 5.dp,
//        color = neutralus0
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {

            val spacerModifier = Modifier.weight(1f)
            val dividerModifier = Modifier.width(1.dp).fillMaxHeight()

            labels.forEachIndexed { index, plantDetailLabel ->
                PPDetailsItem(
                    smallLabel = plantDetailLabel.label,
                    bigLabel = plantDetailLabel.value,
                )
                if (index != labels.size - 1) {
                    Spacer(spacerModifier)
                    Divider(dividerModifier)
                    Spacer(spacerModifier)
                }
            }
        }
    }
}

@Composable
private fun PPDetailsItem(
    modifier: Modifier = Modifier,
    smallLabel: String,
    bigLabel: String
){
    Column(
        modifier = modifier
    ) {
        Text(
            text = smallLabel,
            textAlign = TextAlign.Left,
//            color = neutralus500,
            style = MaterialTheme.typography.titleMedium,

            )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = bigLabel,
            textAlign = TextAlign.Left,
//            color = accent500,
            style = MaterialTheme.typography.bodyMedium,

            )
    }
}


@Preview
@Composable
private fun PlantDetailsBarPreview() {

    val labels = listOf(
        DetailItem(label = "Size", value = "Medium"),
        DetailItem(label = "Water", value = "250ml"),
        DetailItem(label = "Frequency", value = "2 times/week"),
    )

    ThemeSurfaceWrapper {
        BannerDetails(
            labels = labels,
            modifier = Modifier
                .wrapContentSize()
                .padding(20.dp)
        )
    }
}