package com.sunrisekcdeveloper.pureplanting.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.sunrisekcdeveloper.pureplanting.app.ui.theme.PurePlantingTheme
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestackextensions.fragments.KeyedFragment
import com.zhuinden.simplestackextensions.fragmentsktx.backstack

abstract class ComposeFragment : KeyedFragment() {
    @Composable
    abstract fun FragmentComposable(backstack: Backstack)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val backstack = backstack

        return ComposeView(requireContext()).apply {
            // Dispose the Composition when the view's LifecycleOwner is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ThemeSurfaceWrapper { FragmentComposable(backstack) }
            }
        }
    }
}

@Composable
fun ThemeSurfaceWrapper(
    content: @Composable () -> Unit,
) {
    PurePlantingTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) { content() }
    }
}