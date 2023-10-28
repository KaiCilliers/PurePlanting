package com.sunrisekcdeveloper.pureplanting.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import com.sunrisekcdeveloper.pureplanting.R
import com.sunrisekcdeveloper.pureplanting.app.ui.theme.PurePlantingTheme
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestackextensions.fragments.DefaultFragmentStateChanger

class MainActivity : FragmentActivity() {

    private lateinit var fragmentStateChanger: DefaultFragmentStateChanger
    private lateinit var backstack: Backstack

    private val backPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            backstack.goBack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}