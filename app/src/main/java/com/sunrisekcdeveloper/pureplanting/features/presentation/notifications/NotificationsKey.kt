package com.sunrisekcdeveloper.pureplanting.features.presentation.notifications

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.pureplanting.navigation.FragmentKey
import kotlinx.parcelize.Parcelize

@Parcelize
data object NotificationsKey : FragmentKey() {
    override fun instantiateFragment(): Fragment = NotificationsFragment()
}