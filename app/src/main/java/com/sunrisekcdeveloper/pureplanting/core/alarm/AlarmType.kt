package com.sunrisekcdeveloper.pureplanting.core.alarm

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class AlarmType(
    open val name: String
) : Parcelable {
    @Parcelize
    data object ForgotToWater: AlarmType(KEY_FORGOT_TO_WATER)

    @Parcelize
    data object NeedsWater: AlarmType(KEY_NEEDS_WATER)

    companion object {
        const val KEY_FORGOT_TO_WATER = "ForgotToWater"
        const val KEY_NEEDS_WATER = "NeedsWater"

        fun String.toAlarmType(): AlarmType? {
            return when(this) {
                KEY_FORGOT_TO_WATER -> ForgotToWater
                KEY_NEEDS_WATER -> NeedsWater
                else -> null
            }
        }
    }
}