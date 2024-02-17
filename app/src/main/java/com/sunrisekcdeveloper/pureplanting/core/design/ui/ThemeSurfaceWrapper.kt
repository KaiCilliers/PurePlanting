package com.sunrisekcdeveloper.pureplanting.core.design.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.core.design.theme.PurePlantingTheme

@Composable
fun ThemeSurfaceWrapper(
    content: @Composable () -> Unit,
) {
    PurePlantingTheme {
        Surface(
            modifier = Modifier.padding(20.dp)
        ) { content() }
    }
}