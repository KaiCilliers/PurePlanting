package com.sunrisekcdeveloper.pureplanting.library.design.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.library.design.theme.PurePlantingTheme
import com.sunrisekcdeveloper.pureplanting.library.design.theme.accent500
import com.sunrisekcdeveloper.pureplanting.R

@Composable
fun PrimarySmallButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    label: String,
    @DrawableRes leadingIcon: Int? = null
) {
    Button(
        onClick = { onClick() },
        modifier = modifier
            .wrapContentSize(),
        shape = RoundedCornerShape(7.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp
        ),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = accent500 // todo with MaterialTheme color
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon == null) {
                Image(
                    painter = painterResource(id = R.drawable.upload_icon),
                    contentDescription = ""
                )
            }

            Text(
                text = label,
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 3.dp)
                    .padding(horizontal = 12.dp)
            )
        }
    }
}

@Preview
@Composable
fun PrimarySmallButton_Preview() {
    PurePlantingTheme {
        PrimarySmallButton(onClick = { }, enabled = true, label = "Press Me!")
    }
}