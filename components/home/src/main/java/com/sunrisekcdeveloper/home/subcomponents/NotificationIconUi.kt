package com.sunrisekcdeveloper.home.subcomponents

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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.components.home.R
import com.sunrisekcdeveloper.design.theme.PurePlantingTheme
import com.sunrisekcdeveloper.design.theme.neutralus100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationIconUi(
    viewModel: NotificationIconViewModel,
    modifier: Modifier = Modifier,
) {

    val isBadgeVisible by viewModel.isNotificationBadgeVisible.collectAsState()

    Box(
        modifier
            .height(IntrinsicSize.Min)
            .aspectRatio(1f)
    ) {
        Surface(
            modifier = Modifier
                .clickable { viewModel.onIconClick() },
            color = neutralus100,
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
                    .fillMaxSize(0.25f)
            )
        }
    }
}

@Preview
@Composable
private fun NotificationIconUi_Preview() {
    PurePlantingTheme {
        NotificationIconUi(
            NotificationIconViewModel.Fake(),
            modifier = Modifier.size(100.dp)
        )
    }
}