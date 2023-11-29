package com.sunrisekcdeveloper.pureplanting.app

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sunrisekcdeveloper.navigation.FragmentStateChanger
import com.sunrisekcdeveloper.pureplanting.R
import com.sunrisekcdeveloper.pureplanting.features.PlantsKey
import com.sunrisekcdeveloper.reminders.WaterPlantReminder
import com.zhuinden.simplestack.BackHandlingModel
import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestack.History
import com.zhuinden.simplestack.SimpleStateChanger
import com.zhuinden.simplestack.StateChange
import com.zhuinden.simplestack.navigator.Navigator
import com.zhuinden.simplestackextensions.fragments.DefaultFragmentStateChanger
import com.zhuinden.simplestackextensions.lifecyclektx.observeAheadOfTimeWillHandleBackChanged
import com.zhuinden.simplestackextensions.navigatorktx.androidContentFrame
import com.zhuinden.simplestackextensions.services.DefaultServiceProvider
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
            .setScopedServices(DefaultServiceProvider())
            .setGlobalServices(globalServices)
            .install(this, androidContentFrame, History.of(PlantsKey))

        backPressedCallback.isEnabled = backstack.willHandleAheadOfTimeBack()
        backstack.observeAheadOfTimeWillHandleBackChanged(this, backPressedCallback::isEnabled::set)

        schedulePlantWateringWorker()
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
        /**
         * I have two options for this worker
         *
         * 1. schedule a unique worker per plant to execute at a certain time
         * Upon completion calculate the next watering time and schedule another unique worker
         *
         * 2. check periodically (every hour) to see if a plant was forgotten.
         */
        // todo daily executeion at 00:01
        // alternative solution https://copyprogramming.com/howto/schedule-a-work-on-a-specific-time-with-workmanager
        // another is to use Alarm manager for exact time execution
    }

    override fun onNavigationEvent(stateChange: StateChange) {
        fragmentStateChanger.handleStateChange(stateChange)
    }
}