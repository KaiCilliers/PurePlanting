package com.sunrisekcdeveloper.pureplanting.library.design

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp

private val startTopIconPadding = Modifier.padding(top = 30.dp, start = 20.dp)
private val endTopIconPadding = Modifier.padding(top = 30.dp, end = 20.dp)

fun Modifier.topStartIconPadding(): Modifier = this then startTopIconPadding

fun Modifier.topEndIconPadding(): Modifier = this then endTopIconPadding

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}