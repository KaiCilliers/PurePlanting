package com.sunrisekcdeveloper.pureplanting.app

import android.app.Application
import com.zhuinden.simplestack.GlobalServices

class PurePlantingApplication : Application() {
    lateinit var globalServices: GlobalServices
        private set

    override fun onCreate() {
        super.onCreate()

        // Create global dependencies

     globalServices = GlobalServices.builder()
         // add dependencies here
         .build()
    }
}