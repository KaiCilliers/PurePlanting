package com.sunrisekcdeveloper.pureplanting.features.presentation.notifications

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.pureplanting.features.component.notifications.NotificationCache
import com.sunrisekcdeveloper.pureplanting.navigation.FragmentKey
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import kotlinx.parcelize.Parcelize

@Parcelize
data object NotificationsKey : FragmentKey() {
    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            add(NotificationListViewModel(lookup<NotificationCache>()))
        }
    }

    override fun instantiateFragment(): Fragment = NotificationsFragment()
}