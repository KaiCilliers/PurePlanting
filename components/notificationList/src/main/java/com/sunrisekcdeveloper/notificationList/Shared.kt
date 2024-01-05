package com.sunrisekcdeveloper.notificationList

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.notification.Notification

internal typealias NotificationGroupedByDay = Map<Pair<Int, Int>, List<Notification>>

internal val headingPadding = PaddingValues(
    start = 20.dp,
    top = 12.dp
)