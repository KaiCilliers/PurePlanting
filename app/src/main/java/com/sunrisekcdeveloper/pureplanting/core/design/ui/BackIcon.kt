package com.sunrisekcdeveloper.pureplanting.core.design.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.sunrisekcdeveloper.pureplanting.core.design.noRippleClickable
import com.sunrisekcdeveloper.pureplanting.core.design.theme.PurePlantingTheme
import com.sunrisekcdeveloper.pureplanting.R

@Composable
fun BackIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(id = R.drawable.arrow_left_with_border),
        contentDescription = "",
        alignment = Alignment.TopStart,
        modifier = modifier.noRippleClickable { onClick() }
    )
}

@Preview
@Composable
private fun BackIcon_Preview() {
    PurePlantingTheme {
        BackIcon(
            onClick = {}
        )
    }
}