package com.sunrisekcdeveloper.pureplanting.features.presentation.plants

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.pureplanting.navigation.FragmentKey
import kotlinx.parcelize.Parcelize

@Parcelize
data object PlantsKey : FragmentKey() {
    override fun instantiateFragment(): Fragment = PlantsFragment()
}