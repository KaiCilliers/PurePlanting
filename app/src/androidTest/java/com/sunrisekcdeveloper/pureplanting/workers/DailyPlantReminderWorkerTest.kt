package com.sunrisekcdeveloper.pureplanting.workers

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.sunrisekcdeveloper.shared_test.PlantCacheFake
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Test

class DailyPlantReminderWorkerTest {

    private lateinit var context: Context
    private lateinit var plantCacheFake: PlantCacheFake

    @Before
    fun setUp() {
        plantCacheFake = PlantCacheFake()
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun a() {
        val worker = TestListenableWorkerBuilder<DailyPlantReminderWorker>(context)
            .setWorkerFactory(DailyPlantReminderWorkerFactory(plantCacheFake))
            .build()
        runBlocking {
            val result = worker.doWork()
            assertThat(result, `is`(ListenableWorker.Result.success()))
        }
    }
}