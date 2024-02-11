package com.sunrisekcdeveloper.pureplanting.app

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import com.sunrisekcdeveloper.pureplanting.app.workers.DeeplinkDestination
import com.sunrisekcdeveloper.pureplanting.core.design.theme.PurePlantingTheme
import com.sunrisekcdeveloper.pureplanting.core.navigation.ComposeKey
import com.sunrisekcdeveloper.pureplanting.core.navigation.NavigationServiceProvider
import com.sunrisekcdeveloper.pureplanting.domain.notification.NotificationRepository
import com.sunrisekcdeveloper.pureplanting.features.detail.DetailKey
import com.sunrisekcdeveloper.pureplanting.features.home.HomeKey
import com.zhuinden.simplestack.AsyncStateChanger
import com.zhuinden.simplestack.BackHandlingModel
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestack.History
import com.zhuinden.simplestack.navigator.Navigator
import com.zhuinden.simplestackcomposeintegration.core.BackstackProvider
import com.zhuinden.simplestackcomposeintegration.core.ComposeStateChanger
import com.zhuinden.simplestackextensions.lifecyclektx.observeAheadOfTimeWillHandleBackChanged
import com.zhuinden.simplestackextensions.navigatorktx.androidContentFrame
import com.zhuinden.simplestackextensions.servicesktx.get
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {

    private val composeStateChanger = ComposeStateChanger()
    private lateinit var backstack: Backstack

    private val backPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            backstack.goBack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val app = application as PurePlantingApplication
        val globalServices = app.globalServices

        backstack = Navigator.configure()
            .setBackHandlingModel(BackHandlingModel.AHEAD_OF_TIME)
            .setStateChanger(AsyncStateChanger(composeStateChanger))
            .setScopedServices(NavigationServiceProvider())
            .setGlobalServices(globalServices)
            .install(this, androidContentFrame, initialBackStack())

        setupBackHandler()

        setContent {
            BackstackProvider(backstack) {
                PurePlantingTheme {
                    composeStateChanger.RenderScreen()
                }
            }
        }
    }

    private fun markNotificationAsSeen(notificationId: String) {
        MainScope().launch {
            (application as PurePlantingApplication)
                .globalServices
                .get<NotificationRepository>()
                .markAsSeen(notificationId)
        }
    }

    private fun setupBackHandler() {
        onBackPressedDispatcher.addCallback(backPressedCallback)
        backPressedCallback.isEnabled = backstack.willHandleAheadOfTimeBack()
        backstack.observeAheadOfTimeWillHandleBackChanged(this, backPressedCallback::isEnabled::set)
    }

    private fun initialBackStack(): History<ComposeKey> {
        return when (val deeplinkData = intent.getParcelableExtra<DeeplinkDestination>(DEEPLINK_KEY)) {
            is DeeplinkDestination.Detail -> {
                markNotificationAsSeen(deeplinkData.notificationId)
                History.of(HomeKey(), DetailKey(deeplinkData.plant))
            }

            is DeeplinkDestination.Home -> {
                markNotificationAsSeen(deeplinkData.notificationId)
                History.of(HomeKey(deeplinkData.selectedFilter))
            }

            null -> History.of(HomeKey())
        }
    }

    companion object {
        const val DEEPLINK_KEY = "deeplink_key"
    }
}