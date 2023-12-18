package com.sunrisekcdeveloper.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.components.home.R
import com.sunrisekcdeveloper.design.theme.PurePlantingTheme
import com.sunrisekcdeveloper.design.theme.neutralus100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationIcon(
    showBadge: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .height(IntrinsicSize.Min)
            .aspectRatio(1f)
    ) {
        Surface(
            modifier = Modifier
                .padding(10.dp)
                .clickable { onClick() },
            color = neutralus100,
            shape = RoundedCornerShape(percent = 50)
        ) {
            Image(
                painter = painterResource(id = R.drawable.notification),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentDescription = "",
            )
        }
        if (showBadge) {
            Badge(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .fillMaxSize(0.3f)
            )
        }
    }
}

@Preview
@Composable
private fun NotificationIcon_Preview() {
    PurePlantingTheme {
        NotificationIcon(
            showBadge = true,
            onClick = {},
            modifier = Modifier.size(300.dp)
        )
    }
}