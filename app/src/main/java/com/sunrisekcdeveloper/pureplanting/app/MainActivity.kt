package com.sunrisekcdeveloper.pureplanting.app

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sunrisekcdeveloper.navigation.FragmentStateChanger
import com.sunrisekcdeveloper.pureplanting.R
import com.sunrisekcdeveloper.pureplanting.app.workers.ForgotToWaterReminder
import com.sunrisekcdeveloper.pureplanting.app.workers.WaterPlantReminder
import com.sunrisekcdeveloper.pureplanting.features.MainKey
import com.zhuinden.simplestack.BackHandlingModel
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestack.History
import com.zhuinden.simplestack.SimpleStateChanger
import com.zhuinden.simplestack.StateChange
import com.zhuinden.simplestack.navigator.Navigator
import com.zhuinden.simplestackextensions.fragments.DefaultFragmentStateChanger
import com.zhuinden.simplestackextensions.lifecyclektx.observeAheadOfTimeWillHandleBackChanged
import com.zhuinden.simplestackextensions.navigatorktx.androidContentFrame
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class MainActivity : FragmentActivity(), SimpleStateChanger.NavigationHandler {

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

        onBackPressedDispatcher.addCallback(backPressedCallback)

        val app = application as PurePlantingApplication
        val globalServices = app.globalServices

        fragmentStateChanger = FragmentStateChanger(supportFragmentManager, R.id.container)

        backstack = Navigator.configure()
            .setBackHandlingModel(BackHandlingModel.AHEAD_OF_TIME)
            .setStateChanger(SimpleStateChanger(this))
            .setScopedServices(NavigationServiceProvider())
            .setGlobalServices(globalServices)
            .install(this, androidContentFrame, History.of(MainKey()))

        backPressedCallback.isEnabled = backstack.willHandleAheadOfTimeBack()
        backstack.observeAheadOfTimeWillHandleBackChanged(this, backPressedCallback::isEnabled::set)

        schedulePlantWateringWorker()
        scheduleForgotToWaterReminder()
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

    override fun onNavigationEvent(stateChange: StateChange) {
        fragmentStateChanger.handleStateChange(stateChange)
    }
}