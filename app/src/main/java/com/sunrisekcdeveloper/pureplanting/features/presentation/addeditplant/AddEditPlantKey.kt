package com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.pureplanting.navigation.FragmentKey
import kotlinx.parcelize.Parcelize

@Parcelize
data object AddEditPlantKey : FragmentKey() {
    override fun instantiateFragment(): Fragment = AddEditPlantFragment()
}