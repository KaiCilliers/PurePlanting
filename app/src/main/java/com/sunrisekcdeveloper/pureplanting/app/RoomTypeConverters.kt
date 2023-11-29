package com.sunrisekcdeveloper.pureplanting.app

import androidx.room.TypeConverter
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

class RoomTypeConverters {
    @TypeConverter
    fun fromTimeStampToDate(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromTimeStampToTime(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it) }
    }

    @TypeConverter
    fun timeToTimestamp(time: LocalTime?): String? {
        return time?.toString()
    }

    @TypeConverter
    fun fromStringToDayOfWeeks(value: String?): List<DayOfWeek>? {
        return value?.split(",")
            ?.toList()
            ?.map { DayOfWeek.valueOf(it.trimStart()) }
    }

    @TypeConverter
    fun daysOfWeekToString(daysOfWeek: List<DayOfWeek>?): String? {
        return daysOfWeek?.joinToString(",") { it.toString() }
    }

    @TypeConverter
    fun stringToListOfStrings(value: String?): List<String>? {
        return value
            ?.split(",")
            ?.toList()
    }

    @TypeConverter
    fun listOfStringsToList(list: List<String>?): String? {
        return list?.joinToString(",")
    }
}

suspend fun main() {
    val s = DayOfWeek.MONDAY.toString()
    println(DayOfWeek.valueOf(s))
}