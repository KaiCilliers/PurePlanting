package com.sunrisekcdeveloper.pureplanting.features

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.navigation.FragmentKey
import com.sunrisekcdeveloper.notification.NotificationCache
import com.sunrisekcdeveloper.notificationList.NotificationListViewModel
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