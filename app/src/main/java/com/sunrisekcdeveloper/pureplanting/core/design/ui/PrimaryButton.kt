package com.sunrisekcdeveloper.pureplanting.core.design.ui

import androidx.compose.foundation.layout.padding
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

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    label: String
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        shape = RoundedCornerShape(7.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp
        ),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
//            containerColor = accent500 // todo with MaterialTheme color
        )
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.background,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 5.dp)
                .padding(horizontal = 20.dp)
        )
    }
}

@Preview
@Composable
fun PrimaryButton_Preview() {
    PurePlantingTheme {
        PrimaryButton(onClick = { }, enabled = true, label = "Press Me!")
    }
}