package com.sunrisekcdeveloper.pureplanting.components.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.sunrisekcdeveloper.pureplanting.core.design.ui.PrimaryButton

@Composable
internal fun DetailSheet(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onButtonClick: () -> Unit,
    needsWaterToday: Boolean,
) {
    Column(modifier) {
        Surface(
//            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.displayLarge,
//                    color = neutralus900,
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.displaySmall,
//                    color = neutralus500,
                    modifier = Modifier.wrapContentHeight().fillMaxWidth(),
                    lineHeight = 1.5.em,
                )
            }
        }
        Surface(
//            color = MaterialTheme.colorScheme.secondaryContainer,
        ) {
            PrimaryButton(
                onClick = { onButtonClick() },
                label = "Mark as Watered", // todo string resource
                enabled = needsWaterToday,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
            )
        }
    }
}