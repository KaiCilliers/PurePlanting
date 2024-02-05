package com.sunrisekcdeveloper.alarm.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class AlarmType(
    open val name: String
) : Parcelable {
    @Parcelize
    data class ForgotToWater(
        override val name: String = KEY_FORGOT_TO_WATER
    ) : AlarmType(name)

    @Parcelize
    data class NeedsWater(
        override val name: String = KEY_NEEDS_WATER
    ) : AlarmType(name)

    companion object {
        const val KEY_FORGOT_TO_WATER = "ForgotToWater"
        const val KEY_NEEDS_WATER = "NeedsWater"

        fun String.toAlarmType(): AlarmType? {
            return when(this) {
                KEY_FORGOT_TO_WATER -> ForgotToWater()
                KEY_NEEDS_WATER -> NeedsWater()
                else -> null
            }
        }
    }
}