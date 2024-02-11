@file:OptIn(ExperimentalFoundationApi::class)

package com.sunrisekcdeveloper.pureplanting.core.design.theme

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LocalColors = staticCompositionLocalOf { LightColorScheme }

private val LightColorScheme: MyColors
    get() {
        val colorScheme = lightColorScheme(
            primary = accent600,
            onPrimary = neutralus0,
            secondary = accent100,
            onSecondary = neutralus0,
            tertiary = neutralus100,
            onTertiary = neutralus500,
            surface = neutralus100,
            onSurface = neutralus900,
        )
        return MyColors(
            material = colorScheme,
            focused = accent600,
            unfocused = neutralus300,
            plantCardBackground = otherOlive100,
            tag = neutralus300.copy(alpha = 0.6f),
        )
    }

@Composable
fun PurePlantingTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    CompositionLocalProvider(LocalColors provides LightColorScheme) {
        MaterialTheme(
            colorScheme = LightColorScheme.material,
            content = {
                CompositionLocalProvider(
                    LocalOverscrollConfiguration provides null,
                    content = content
                )
            }
        )
    }
}

data class MyColors(
    val material: ColorScheme,
    val focused: Color,
    val unfocused: Color,
    val plantCardBackground: Color,
    val tag: Color,
) {
    val primary: Color get() = material.primary
    val primaryContainer: Color get() = material.primaryContainer
    val secondary: Color get() = material.secondary
    val secondaryContainer: Color get() = material.secondaryContainer
    val background: Color get() = material.background
    val surface: Color get() = material.surface
    val error: Color get() = material.error
    val onPrimary: Color get() = material.onPrimary
    val onSecondary: Color get() = material.onSecondary
    val onBackground: Color get() = material.onBackground
    val onSurface: Color get() = material.onSurface
    val onError: Color get() = material.onError
}

val MaterialTheme.ppColors: MyColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current