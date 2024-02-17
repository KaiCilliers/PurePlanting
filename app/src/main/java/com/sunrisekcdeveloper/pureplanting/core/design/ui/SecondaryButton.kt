package com.sunrisekcdeveloper.pureplanting.core.design.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.core.design.theme.PurePlantingTheme
import com.sunrisekcdeveloper.pureplanting.core.design.theme.ppColors
import com.sunrisekcdeveloper.pureplanting.core.design.theme.ppTypography

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    label: String
) {
    Button(
        onClick = { onClick() },
        modifier = modifier
            .wrapContentSize(),
        shape = RoundedCornerShape(7.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.ppColors.onPrimaryMuted),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp
        ),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.ppColors.surface,
        )
    ) {
        Text(
            text = label,
            color = MaterialTheme.ppColors.onSurfaceSecondary,
            style = MaterialTheme.ppTypography.headingSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 5.dp)
                .padding(horizontal = 20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SecondaryButton_Preview() {
    ThemeSurfaceWrapper {
        SecondaryButton(onClick = { }, enabled = true, label = "Press Me!")
    }
}