package com.sunrisekcdeveloper.shared_test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.time.Duration
import kotlin.time.toJavaDuration

@OptIn(ExperimentalCoroutinesApi::class)
fun TestScope.advanceTimeBy(duration: Duration, clock: MutableClock) {
    advanceTimeBy(duration)
    clock.advanceTimeBy(duration.toJavaDuration())
}