package com.sunrisekcdeveloper.pureplanting.features.presentation.notifications

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.sunrisekcdeveloper.pureplanting.navigation.ComposeFragment
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestackextensions.servicesktx.lookup
import java.time.format.DateTimeFormatter

class NotificationsFragment : ComposeFragment() {
    @Composable
    override fun FragmentComposable(backstack: Backstack) {
        val viewModel = remember { backstack.lookup<NotificationListViewModel>() }
        val activeFilter = viewModel.activeFilter.collectAsState()
        val notificationsGrouped = viewModel.notificationsGroupedByDay.collectAsState()
        val context = LocalContext.current

        NotificationListScreen(
            filterOptions = NotificationFilter.values().toList(),
            selectedFilter = activeFilter,
            onFilterSelected = { viewModel.setFilter(it) },
            notifications = notificationsGrouped,
            onNotificationClick = {
                Toast.makeText(context, "tapped a notification created at ${it.created.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a"))}", Toast.LENGTH_SHORT).show()
            },
            onBackButtonClick = { backstack.goBack() }
        )
    }
}