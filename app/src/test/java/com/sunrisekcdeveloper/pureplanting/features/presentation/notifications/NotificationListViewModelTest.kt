package com.sunrisekcdeveloper.pureplanting.features.presentation.notifications

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.sunrisekcdeveloper.shared_test.MutableClock
import com.sunrisekcdeveloper.shared_test.NotificationCacheFake
import com.sunrisekcdeveloper.shared_test.forgotToWaterNotification
import com.sunrisekcdeveloper.shared_test.now
import com.sunrisekcdeveloper.shared_test.today
import com.sunrisekcdeveloper.shared_test.unreadNotification
import com.sunrisekcdeveloper.shared_test.waterSoonNotification
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
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationListViewModelTest {

    private lateinit var viewModel: NotificationListViewModel
    private lateinit var notificationCacheFake: NotificationCacheFake
    private lateinit var mutableClock: MutableClock

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        notificationCacheFake = NotificationCacheFake()
        mutableClock = MutableClock(Clock.systemDefaultZone())
        viewModel = NotificationListViewModel(notificationCacheFake)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        mutableClock.reset()
    }

    @Test
    fun `initial launch has all notification filter selected`() = runTest {
        // SETUP
        val filterFlow = viewModel.activeFilter

        // ASSERTIONS
        filterFlow.test {
            assertThat(awaitItem()).isEqualTo(NotificationFilter.ALL)
        }
    }


    @Test
    fun `all notifications are displayed when all notifications filter is selected`() = runTest {
        // SETUP
        val notificationsFlow = viewModel.notifications

        notificationCacheFake.save(waterSoonNotification())
        notificationCacheFake.save(forgotToWaterNotification())
        notificationCacheFake.save(forgotToWaterNotification())
        notificationCacheFake.save(waterSoonNotification())
        notificationCacheFake.save(forgotToWaterNotification())

        // ACTION
        viewModel.setFilter(NotificationFilter.ALL)

        // ASSERTIONS
        notificationsFlow.test {
            awaitItem()

            val emission = awaitItem()
            assertThat(emission.size).isEqualTo(5)
        }
    }


    @Test
    fun `only notifications with type forgot-to-water is displayed when forgot to water filter is selected`() = runTest {
        // SETUP
        val notificationsFlow = viewModel.notifications

        notificationCacheFake.save(waterSoonNotification())
        notificationCacheFake.save(forgotToWaterNotification())
        notificationCacheFake.save(forgotToWaterNotification())
        notificationCacheFake.save(waterSoonNotification())
        notificationCacheFake.save(forgotToWaterNotification())

        // ACTION
        viewModel.setFilter(NotificationFilter.FORGOT_TO_WATER)

        // ASSERTIONS
        notificationsFlow.test {
            awaitItem()

            val emission = awaitItem()
            assertThat(emission.size).isEqualTo(3)
        }
    }

    @Test
    fun `only notifications with type needs-water is displayed when needs water filter is selected`() = runTest {
        // SETUP
        val notificationsFlow = viewModel.notifications

        notificationCacheFake.save(waterSoonNotification())
        notificationCacheFake.save(forgotToWaterNotification())
        notificationCacheFake.save(forgotToWaterNotification())
        notificationCacheFake.save(waterSoonNotification())
        notificationCacheFake.save(forgotToWaterNotification())

        // ACTION
        viewModel.setFilter(NotificationFilter.NEEDS_WATER)

        // ASSERTIONS
        notificationsFlow.test {
            awaitItem()

            val emission = awaitItem()
            assertThat(emission.size).isEqualTo(2)
        }
    }

    @Test
    fun `notifications are always sorted by their date created with the most recent notifications appearing at the top of the list`() = runTest {
        // SETUP
        val notificationsFlow = viewModel.notifications

        mutableClock.reverseTimeBy(1.hours.toJavaDuration())
        notificationCacheFake.save(
            unreadNotification(
                created = now(mutableClock),
                id = "#4"
            )
        )

        mutableClock.advanceTimeBy(10.minutes.toJavaDuration())
        notificationCacheFake.save(
            unreadNotification(
                created = now(mutableClock),
                id = "#3",
            )
        )

        mutableClock.advanceTimeBy(10.minutes.toJavaDuration())
        notificationCacheFake.save(
            unreadNotification(
                created = now(mutableClock),
                id = "#2",
            )
        )

        mutableClock.reverseTimeBy(2.hours.toJavaDuration())
        notificationCacheFake.save(
            unreadNotification(
                created = now(mutableClock),
                id = "#5"
            )
        )

        mutableClock.reset()
        mutableClock.advanceTimeBy(1.hours.toJavaDuration())
        notificationCacheFake.save(
            unreadNotification(
                created = now(mutableClock),
                id = "#1",
            )
        )

        // ASSERTIONS
        notificationsFlow.test {
            awaitItem()

            val emission = awaitItem()
            assertThat(emission.size).isEqualTo(5)
            assertThat(emission[0].id).isEqualTo("#1")
            assertThat(emission[1].id).isEqualTo("#2")
            assertThat(emission[2].id).isEqualTo("#3")
            assertThat(emission[3].id).isEqualTo("#4")
            assertThat(emission[4].id).isEqualTo("#5")
        }
    }

    @Test
    fun `switching filters updates all previously visible notifications as seen`() = runTest {
        // SETUP
        val notificationsFlow = viewModel.notifications

        notificationCacheFake.save(forgotToWaterNotification())
        notificationCacheFake.save(forgotToWaterNotification())
        notificationCacheFake.save(forgotToWaterNotification())
        notificationCacheFake.save(waterSoonNotification())
        notificationCacheFake.save(forgotToWaterNotification())

        // ACTION
        viewModel.setFilter(NotificationFilter.ALL)

        // ASSERTIONS
        notificationsFlow.test {
            awaitItem()

            val emission = awaitItem()
            assertThat(emission.size).isEqualTo(5)

            viewModel.setFilter(NotificationFilter.FORGOT_TO_WATER)

            val emission2 = awaitItem()
            assertThat(emission2.size).isEqualTo(4)
            assertThat(emission2.all { it.seen }).isTrue()
        }
    }

    @Test
    fun `notifications are grouped by their day created`() = runTest {
        // SETUP
        val notificationsFlow = viewModel.notificationsGroupedByDay
        val today = today()
        val yesterday = today.minusDays(1)
        val twoDaysAgo = today.minusDays(2)
        val currentYear = today.year
        val currentDayOfYear = today.dayOfYear

        notificationCacheFake.save(waterSoonNotification(created = yesterday))
        notificationCacheFake.save(forgotToWaterNotification(created = today))
        notificationCacheFake.save(forgotToWaterNotification(created = yesterday))
        notificationCacheFake.save(waterSoonNotification(created = twoDaysAgo))
        notificationCacheFake.save(forgotToWaterNotification(created = twoDaysAgo))

        // ASSERTIONS
        notificationsFlow.test {
            awaitItem()

            val emission = awaitItem()
            assertThat(emission.size).isEqualTo(3)
            assertThat(emission[(currentDayOfYear to currentYear)]!!.size).isEqualTo(1)
            assertThat(emission[(currentDayOfYear - 1 to currentYear)]!!.size).isEqualTo(2)
            assertThat(emission[(currentDayOfYear - 2 to currentYear)]!!.size).isEqualTo(2)
        }
    }

    @Test
    fun `notifications are grouped by their day created with the latest date placed first`() = runTest {
        // SETUP
        val notificationsFlow = viewModel.notificationsGroupedByDay
        val today = today()
        val yesterday = today.minusDays(1)
        val oneYearAgo = today.minusYears(1)
        val currentYear = today.year
        val currentDayOfYear = today.dayOfYear

        notificationCacheFake.save(waterSoonNotification(created = yesterday))
        notificationCacheFake.save(forgotToWaterNotification(created = today))
        notificationCacheFake.save(forgotToWaterNotification(created = yesterday))
        notificationCacheFake.save(waterSoonNotification(created = oneYearAgo))
        notificationCacheFake.save(forgotToWaterNotification(created = oneYearAgo))

        // ASSERTIONS
        notificationsFlow.test {
            awaitItem()

            val emission = awaitItem()
            val keys = emission.keys
            assertThat(emission.size).isEqualTo(3)

            assertThat(keys.elementAt(0)).isEqualTo(currentDayOfYear to currentYear)
            assertThat(keys.elementAt(1)).isEqualTo(currentDayOfYear - 1 to currentYear)
            assertThat(keys.elementAt(2)).isEqualTo(currentDayOfYear to currentYear - 1)

            assertThat(emission[keys.elementAt(0)]!!.size).isEqualTo(1) // today
            assertThat(emission[keys.elementAt(1)]!!.size).isEqualTo(2) // yesterday
            assertThat(emission[keys.elementAt(2)]!!.size).isEqualTo(2) // year ago
        }
    }
}