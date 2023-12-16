package com.sunrisekcdeveloper.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper

@Composable
fun PPDetailsItem(
    modifier: Modifier = Modifier,
    smallLabel: String,
    bigLabel: String
){
    Column(
        modifier = modifier
    ) {
        Text(
            text = smallLabel,
            textAlign = TextAlign.Left,
            color = MaterialTheme.colorScheme.onBackground, // todo correct color scheme (gray)
            style = MaterialTheme.typography.titleMedium,

            )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = bigLabel,
            textAlign = TextAlign.Left,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium,

        )
    }
}

@Preview
@Composable
fun DetailsItemPreview() {
    ThemeSurfaceWrapper {
        PPDetailsItem(smallLabel = "Size", bigLabel = "Medium")
    }
}