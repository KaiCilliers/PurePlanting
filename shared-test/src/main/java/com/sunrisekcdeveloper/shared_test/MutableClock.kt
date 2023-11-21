package com.sunrisekcdeveloper.shared_test

import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class MutableClock(private val origin: Clock): Clock(){

    private var offset: Duration = Duration.ZERO

    fun advanceTimeBy(duration: Duration) {
        offset = offset.plus(duration)
    }

    fun reverseTimeBy(duration: Duration) {
        offset = offset.minus(duration)
    }

    fun reset() {
        offset = Duration.ZERO
    }

    override fun getZone(): ZoneId {
        return origin.zone
    }

    override fun withZone(zoneId: ZoneId?): Clock {
        return MutableClock(origin.withZone(zoneId)).also {
            it.offset = offset
        }
    }

    override fun instant(): Instant {
        return origin.instant().plus(offset)
    }
}