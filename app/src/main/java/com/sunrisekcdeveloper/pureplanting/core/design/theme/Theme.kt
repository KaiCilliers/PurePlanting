@file:OptIn(ExperimentalFoundationApi::class)

package com.sunrisekcdeveloper.pureplanting.core.design.theme

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

private val LocalColors = staticCompositionLocalOf { LightColorScheme }
private val LocalTypography = staticCompositionLocalOf { myTypography }

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
            surface = neutralus0,
            onSurfacePrimary = neutralus900,
            onSurfaceSecondary = neutralus500,
            onSurfaceMuted = neutralus300,
            primary = accent600,
            onPrimary = neutralus0,
            primaryMuted = otherOlive500.copy(alpha = 0.2f),
            onPrimaryMuted = neutralus100,
            tagBackground = neutralus300.copy(alpha = 0.6f),
            onTagBackground = neutralus100,
            alert = red,
            inputFieldBackground = neutralus100,
            error = red,
        )
    }

private val myTypography: MyTypography
    get() {
        return MyTypography(
            material = Typography(),
            // H1/ Semibold / 24px / 32px
            title = TextStyle(
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                lineHeight = 32.sp,
            ),

            // Body/B1 16-24 Medium
            headingSmall = TextStyle(
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),

            // Body B2 / Semibold / 14px/20px
            bodyMedium = TextStyle(
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),

            // Caption/C2 10-16 Medium
            captionSmall = TextStyle(
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                lineHeight = 15.sp,
            ),

            // Caption/$C1 12-16 Regular
            captionMedium = TextStyle(
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),

            // Body/$B2 14-20 Semibold
            cardTitle = TextStyle(
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),

            // Caption/$C1 12-16 Regular
            cardCaption = TextStyle(
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),

            // Body/B1 16-24 Medium
            dialogTitle = TextStyle(
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),

            // Body/$B3 14-20 Medium
            dialogBody = TextStyle(
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),

            // Body/$B3 14-20 Medium
            bodySmall = TextStyle(
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
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

    CompositionLocalProvider(
        LocalColors provides LightColorScheme,
        LocalTypography provides myTypography,
    ) {
        MaterialTheme(
            colorScheme = LightColorScheme.material,
            typography = myTypography.material,
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
    val surface: Color,
    val primary: Color,
    val onSurfacePrimary: Color,
    val onSurfaceSecondary: Color,
    val onSurfaceMuted: Color,
    val primaryMuted: Color,
    val onPrimaryMuted: Color,
    val tagBackground: Color,
    val onTagBackground: Color,
    val alert: Color,
    val onPrimary: Color,
    val inputFieldBackground: Color,
    val error: Color,
) {
    val primaryContainer: Color get() = material.primaryContainer
    val secondary: Color get() = material.secondary
    val secondaryContainer: Color get() = material.secondaryContainer
    val background: Color get() = material.background
    val onSecondary: Color get() = material.onSecondary
    val onBackground: Color get() = material.onBackground
    val onError: Color get() = material.onError
}

val MaterialTheme.ppTypography: MyTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalTypography.current

val MaterialTheme.ppColors: MyColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current