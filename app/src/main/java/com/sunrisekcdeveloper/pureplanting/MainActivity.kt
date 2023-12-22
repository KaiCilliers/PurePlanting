package com.sunrisekcdeveloper.pureplanting

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sunrisekcdeveloper.design.theme.PurePlantingTheme
import com.sunrisekcdeveloper.design.ui.ObserveAsEvents
import com.sunrisekcdeveloper.design.ui.SnackbarEmitter
import com.sunrisekcdeveloper.design.ui.SnackbarEmitterType
import com.sunrisekcdeveloper.pureplanting.features.HomeKey
import com.sunrisekcdeveloper.pureplanting.navigation.NavigationServiceProvider
import com.sunrisekcdeveloper.pureplanting.workers.ForgotToWaterReminder
import com.sunrisekcdeveloper.pureplanting.workers.WaterPlantReminder
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
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

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

        onBackPressedDispatcher.addCallback(backPressedCallback)

        val app = application as PurePlantingApplication
        val globalServices = app.globalServices

        backstack = Navigator.configure()
            .setBackHandlingModel(BackHandlingModel.AHEAD_OF_TIME)
            .setStateChanger(AsyncStateChanger(composeStateChanger))
            .setScopedServices(NavigationServiceProvider())
            .setGlobalServices(globalServices)
            .install(this, androidContentFrame, History.of(HomeKey()))

        backPressedCallback.isEnabled = backstack.willHandleAheadOfTimeBack()
        backstack.observeAheadOfTimeWillHandleBackChanged(this, backPressedCallback::isEnabled::set)

        schedulePlantWateringWorker()
        scheduleForgotToWaterReminder()

        setContent {
            BackstackProvider(backstack) {
                PurePlantingTheme {
                    
                    val snackbarHostState = remember { SnackbarHostState() }
                    val scope = rememberCoroutineScope()
                    val context = LocalContext.current
                    
                    Scaffold(
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { contentPadding ->

                        ObserveAsEvents(flow = globalServices.get<SnackbarEmitter>().eventFlow) { eventType ->
                            scope.launch { 
                                when(eventType) {
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
                                            SnackbarResult.Dismissed -> { }
                                        }
                                    }
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .padding(contentPadding)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            composeStateChanger.RenderScreen()
                        }
                    }

                }
            }
        }
    }

    private fun schedulePlantWateringWorker() {
        val request = PeriodicWorkRequestBuilder<WaterPlantReminder>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES,
        )
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                WaterPlantReminder.TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }

    private fun scheduleForgotToWaterReminder() {
        val today = LocalDateTime.now()
        val tomorrow = LocalDateTime.of(today.toLocalDate().plusDays(1), LocalTime.MIDNIGHT)
        val millisToTomorrow = Duration.between(today, tomorrow.plusHours(2)).toMillis()

        val request = PeriodicWorkRequestBuilder<ForgotToWaterReminder>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setInitialDelay(millisToTomorrow, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                ForgotToWaterReminder.TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }
}