package com.sunrisekcdeveloper.detail.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper

@Composable
fun PPDetailsBanner(
    labels: List<PlantDetailLabel>,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
        tonalElevation = 5.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            labels.forEachIndexed { index, plantDetailLabel ->
                PPDetailsItem(
                    smallLabel = plantDetailLabel.label,
                    bigLabel = plantDetailLabel.value,
                )
                if (index != labels.size - 1) {
                    Spacer(modifier = Modifier.weight(1f))
                    Divider(modifier = Modifier.width(1.dp).height(50.dp))
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}


@Preview
@Composable
fun PlantDetailsBarPreview() {

    val labels = listOf(
        PlantDetailLabel(label = "Size", value = "Medium"),
        PlantDetailLabel(label = "Water", value = "250ml"),
        PlantDetailLabel(label = "Frequency", value = "2 times/week"),
    )

    ThemeSurfaceWrapper {
        PPDetailsBanner(
            labels = labels,
            modifier = Modifier
                .wrapContentSize()
                .padding(20.dp)
        )
    }
}

data class PlantDetailLabel(
    val label: String,
    val value: String,
)