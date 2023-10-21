package com.sunrisekcdeveloper.pureplanting

import android.net.Uri
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters
import java.util.UUID

data class Plant(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val size: String,
    val description: String,
    val imageSrcUri: String,
    val wateringInfo: WateringInfo,
) {
    fun upcomingWateringDate(): LocalDateTime {
        return LocalDateTime.now()
            .with(TemporalAdjusters.next(wateringInfo.dayOfTheWeek))
            .with(wateringInfo.atHour)
    }
}