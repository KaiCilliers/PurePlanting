package com.sunrisekcdeveloper.pureplanting.features.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.core.design.theme.PurePlantingTheme
import com.sunrisekcdeveloper.pureplanting.R
import com.sunrisekcdeveloper.pureplanting.core.design.theme.ppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationIconUi(
    isBadgeVisible: Boolean,
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
                .clickable { onClick() },
            color = MaterialTheme.ppColors.surface,
            shape = RoundedCornerShape(percent = 50)
        ) {
            Image(
                painter = painterResource(id = R.drawable.notification),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentDescription = "",
            )
        }
        if (isBadgeVisible) {
            Badge(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
                    .fillMaxSize(0.20f)
            )
        }
    }
}

@Preview
@Composable
private fun NotificationIconUi_Preview() {
    PurePlantingTheme {
        NotificationIconUi(
            isBadgeVisible = true,
            onClick = { },
            modifier = Modifier.size(100.dp)
        )
    }
}