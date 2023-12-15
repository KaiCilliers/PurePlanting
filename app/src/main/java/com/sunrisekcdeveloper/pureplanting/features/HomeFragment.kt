package com.sunrisekcdeveloper.pureplanting.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.sunrisekcdeveloper.android.navigation.ComposeFragment
import com.sunrisekcdeveloper.home.HomeViewModel
import com.sunrisekcdeveloper.home.HomeUi
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestackextensions.servicesktx.lookup

class HomeFragment : ComposeFragment() {
    @Composable
    override fun FragmentComposable(backstack: Backstack) {
        val viewModel = remember { backstack.lookup<HomeViewModel>() }

        HomeUi(viewModel)
    }
}