package com.sunrisekcdeveloper.pureplanting.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.sunrisekcdeveloper.android.navigation.ComposeFragment
import com.sunrisekcdeveloper.detail.DetailComponent
import com.sunrisekcdeveloper.detail.DetailUi
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestackextensions.servicesktx.lookup

class DetailFragment : ComposeFragment() {
    @Composable
    override fun FragmentComposable(backstack: Backstack) {
        val component = remember { backstack.lookup<DetailComponent>() }

        DetailUi(component)
    }
}