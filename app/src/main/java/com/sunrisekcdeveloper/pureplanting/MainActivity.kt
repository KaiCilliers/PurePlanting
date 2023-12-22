package com.sunrisekcdeveloper.pureplanting

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sunrisekcdeveloper.design.theme.PurePlantingTheme
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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        composeStateChanger.RenderScreen()
                    }
                }
            }
        }

//        MainScope().launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                withContext(Dispatchers.Main) {
//                    app.globalServices.get<SnackbarEmitter>().snackbarEvent.collect {
//                        println("deadpool - event $it")
////                        Snackbar.make(binding.root, "asdasdas", Snackbar.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
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