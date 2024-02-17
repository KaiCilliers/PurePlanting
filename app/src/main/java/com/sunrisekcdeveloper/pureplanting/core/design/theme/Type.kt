package com.sunrisekcdeveloper.pureplanting.core.design.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sunrisekcdeveloper.pureplanting.R

val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Bold),
    Font(R.font.poppins_semibold, FontWeight.SemiBold)
)

data class MyTypography(
    val material: Typography,
    val title: TextStyle,
    val headingSmall: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
    val captionSmall: TextStyle,
    val captionMedium: TextStyle,
    val cardTitle: TextStyle,
    val cardCaption: TextStyle,
    val dialogTitle: TextStyle,
    val dialogBody: TextStyle,
)