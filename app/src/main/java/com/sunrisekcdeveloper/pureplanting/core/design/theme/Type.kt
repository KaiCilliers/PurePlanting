package com.sunrisekcdeveloper.pureplanting.core.design.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sunrisekcdeveloper.pureplanting.R

// EXAMPLE TEMPLATE
val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Bold),
    Font(R.font.poppins_semibold, FontWeight.SemiBold)
)

// EXAMPLE TEMPLATE
val typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),

    bodyLarge = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),

    bodySmall = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
)