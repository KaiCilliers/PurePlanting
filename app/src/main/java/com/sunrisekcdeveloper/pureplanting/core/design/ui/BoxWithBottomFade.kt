package com.sunrisekcdeveloper.pureplanting.core.design.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import com.sunrisekcdeveloper.pureplanting.core.design.theme.PurePlantingTheme

@Composable
fun BoxWithBottomFade(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
   Box(modifier) {
       content()
       Column {
           Spacer(modifier = Modifier.weight(0.8f))
           Box(
               modifier = Modifier
                   .weight(0.2f)
                   .fillMaxSize()
                   .clip(RectangleShape)
                   .background(
                       brush = Brush.verticalGradient(
                           colors = listOf(
                               Color.Transparent,
//                               neutralus0
                           )
                       )
                   )
           )
       }
   }
}

@Preview
@Composable
private fun BottomGradient_Preview() {
    PurePlantingTheme {
        BoxWithBottomFade {
            Surface(
                color = Color.Red.copy(alpha = 0.3f),
                modifier = Modifier.fillMaxSize()
            ){}
        }
    }
}