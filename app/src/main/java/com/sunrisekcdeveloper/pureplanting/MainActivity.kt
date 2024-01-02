package com.sunrisekcdeveloper.pureplanting

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.sunrisekcdeveloper.design.theme.PurePlantingTheme
import com.sunrisekcdeveloper.design.ui.SnackbarEmitter
import com.sunrisekcdeveloper.design.ui.SnackbarEmitterType
import com.sunrisekcdeveloper.notification.NotificationRepository
import com.sunrisekcdeveloper.pureplanting.features.DetailKey
import com.sunrisekcdeveloper.pureplanting.features.HomeKey
import com.sunrisekcdeveloper.pureplanting.navigation.NavigationServiceProvider
import com.sunrisekcdeveloper.pureplanting.workers.DeeplinkDestination
import com.zhuinden.liveevent.observe
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

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        onBackPressedDispatcher.addCallback(backPressedCallback)

        // todo improvement by moving the logic to mark notification as seen outside Activity
        val initialBackstack = when (val deeplinkData = intent.getParcelableExtra<DeeplinkDestination>(DEEPLINK_KEY)) {
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

        val app = application as PurePlantingApplication
        val globalServices = app.globalServices

        backstack = Navigator.configure()
            .setBackHandlingModel(BackHandlingModel.AHEAD_OF_TIME)
            .setStateChanger(AsyncStateChanger(composeStateChanger))
            .setScopedServices(NavigationServiceProvider())
            .setGlobalServices(globalServices)
            .install(this, androidContentFrame, initialBackstack)

        backPressedCallback.isEnabled = backstack.willHandleAheadOfTimeBack()
        backstack.observeAheadOfTimeWillHandleBackChanged(this, backPressedCallback::isEnabled::set)

        setContent {
            BackstackProvider(backstack) {
                PurePlantingTheme {

                    val snackbarHostState = remember { SnackbarHostState() }
                    val scope = rememberCoroutineScope()
                    val context = LocalContext.current

                    Scaffold(
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                        containerColor = Color.Transparent
                    ) { _ ->

                        val lifecycleOwner = LocalLifecycleOwner.current
                        LaunchedEffect(lifecycleOwner.lifecycle) {
                            globalServices.get<SnackbarEmitter>().snackbarEvents.observe(lifecycleOwner) { eventType ->
                                scope.launch {
                                    when (eventType) {
                                        is SnackbarEmitterType.Text -> snackbarHostState.showSnackbar(eventType.text)
                                        is SnackbarEmitterType.TextRes -> snackbarHostState.showSnackbar(context.getString(eventType.resId))
                                        is SnackbarEmitterType.Undo -> {
                                            val result = snackbarHostState.showSnackbar(
                                                message = eventType.text,
                                                actionLabel = "Undo",
                                                duration = SnackbarDuration.Long,
                                            )

                                            when (result) {
                                                SnackbarResult.ActionPerformed -> eventType.undoAction()
                                                SnackbarResult.Dismissed -> {}
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            composeStateChanger.RenderScreen()
                        }
                    }

                }
            }
        }
    }

    private fun markNotificationAsSeen(notificationId: String) {
        MainScope().launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                (application as PurePlantingApplication).globalServices.get<NotificationRepository>()
                    .markAsSeen(notificationId)
            }
        }
    }

    companion object {
        const val DEEPLINK_KEY = "deeplink_key"
    }
}