package com.sunrisekcdeveloper.pureplanting.components.detail.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.sunrisekcdeveloper.pureplanting.R

@Composable
internal fun PlantPlaceholderImage(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(id = R.drawable.single_plant_placeholder),
        contentDescription = "",
    )
}