package com.sunrisekcdeveloper.pureplanting.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.sunrisekcdeveloper.android.navigation.ComposeFragment
import com.sunrisekcdeveloper.notificationList.NotificationListComponent
import com.sunrisekcdeveloper.notificationList.NotificationListUi
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestackextensions.servicesktx.lookup

class NotificationsFragment : ComposeFragment() {
    @Composable
    override fun FragmentComposable(backstack: Backstack) {
        val component = remember { backstack.lookup<NotificationListComponent>() }

        NotificationListUi(component)
    }
}