package com.sunrisekcdeveloper.pureplanting.features.detail.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.R

@Composable
internal fun EditIcon(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .wrapContentSize(),
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(40.dp)

    ) {
        Image(
            painter = painterResource(id = R.drawable.edit2),
            contentDescription = "",
            modifier = Modifier
                .size(35.dp)
                .padding(5.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)

        )
    }
}