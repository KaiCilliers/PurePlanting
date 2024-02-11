package com.sunrisekcdeveloper.home.com.sunrisekcdeveloper.pureplanting.components.home.subcomponents

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.sunrisekcdeveloper.pureplanting.domain.notification.NotificationRepository
import com.sunrisekcdeveloper.pureplanting.domain.notification.unreadNotification
import com.sunrisekcdeveloper.pureplanting.domain.plant.PlantRepository
import com.sunrisekcdeveloper.pureplanting.components.home.subcomponents.NotificationIconViewModel
import com.sunrisekcdeveloper.pureplanting.MutableClock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationIconViewModelTest {

    private lateinit var plantRepositoryFake: PlantRepository.Fake
    private lateinit var viewModel: NotificationIconViewModel.Default
    private lateinit var mutableClock: MutableClock
    private lateinit var notificationRepositoryFake: NotificationRepository.Fake
    private lateinit var router: NotificationIconViewModel.Router

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        mutableClock = MutableClock(Clock.systemDefaultZone())
        plantRepositoryFake = PlantRepository.Fake()
        notificationRepositoryFake = NotificationRepository.Fake()
        router = NotificationIconViewModel.Router { }
        viewModel = NotificationIconViewModel.Default(notificationRepositoryFake, router)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        plantRepositoryFake.resetData()
    }

    @Test
    fun `badge is visible when unread notifications exist`() = runTest {
        // SETUP
        val unreadNotificationsFlow = viewModel.isNotificationBadgeVisible

        notificationRepositoryFake.save(unreadNotification())
        notificationRepositoryFake.save(unreadNotification())
        notificationRepositoryFake.save(unreadNotification())

        // ACTION & ASSERTIONS
        unreadNotificationsFlow.test {
            awaitItem()

            val emission = awaitItem()
            assertThat(emission).isTrue()
        }
    }

    @Test
    fun `badge is not visible when all notifications have been read`() = runTest {
        // SETUP
        val unreadNotificationsFlow = viewModel.isNotificationBadgeVisible
        val notificationOne = unreadNotification()
        val notificationTwo = unreadNotification()

        notificationRepositoryFake.save(notificationOne)
        notificationRepositoryFake.save(notificationTwo)

        // ACTION & ASSERTIONS
        unreadNotificationsFlow.test {
            awaitItem()

            val emission = awaitItem()
            assertThat(emission).isTrue()

            notificationRepositoryFake.save(notificationOne.copy(seen = true))
            notificationRepositoryFake.save(notificationTwo.copy(seen = true))

            val emission3 = awaitItem()
            assertThat(emission3).isFalse()
        }
    }

    @Test
    fun `badge is not visible when zero notifications exist`() = runTest {
        // SETUP
        val unreadNotificationsFlow = viewModel.isNotificationBadgeVisible

        notificationRepositoryFake.resetData(emptyList())

        // ACTION & ASSERTIONS
        unreadNotificationsFlow.test {
            val emission = awaitItem()
            assertThat(emission).isFalse()
        }
    }
}