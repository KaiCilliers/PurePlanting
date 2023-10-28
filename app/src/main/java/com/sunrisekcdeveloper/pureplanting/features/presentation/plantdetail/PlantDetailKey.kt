package com.sunrisekcdeveloper.pureplanting.features.presentation.plantdetail

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.pureplanting.navigation.FragmentKey
import kotlinx.parcelize.Parcelize

@Parcelize
data object PlantDetailKey : FragmentKey() {
    override fun instantiateFragment(): Fragment = PlantDetailFragment()
}