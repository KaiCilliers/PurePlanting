package com.sunrisekcdeveloper.pureplanting.core.design.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TabFilterOption(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {

    val textColor = if (isSelected) Color.Blue else Color.Gray
    val textAlpha = if (isSelected) 1.0f else 0.5f

    Text(
        text = text,
        color = textColor,
        fontSize = 18.sp,
        modifier = modifier
            .alpha(textAlpha)
            .padding(start = 12.dp)
    )
}