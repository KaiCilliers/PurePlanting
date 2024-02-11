package com.sunrisekcdeveloper.pureplanting.core.design.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sunrisekcdeveloper.pureplanting.core.design.theme.PurePlantingTheme

@Composable
fun ThemeSurfaceWrapper(
    content: @Composable () -> Unit,
) {
    PurePlantingTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) { content() }
    }
}