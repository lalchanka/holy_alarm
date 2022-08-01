package com.dmytrokoniev.holyalarm.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    const val ONE_DAY_IN_MILLIS: Long = 86_400_000
    const val ONE_HOUR_IN_MILLIS: Long = 3_600_000
    const val ONE_MIN_IN_MILLIS: Long = 60_000

    fun millisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS", Locale.getDefault())
        val timeZone = TimeZone.getDefault()
        timeZone.rawOffset
        formatter.timeZone = TimeZone.getDefault()
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return formatter.format(calendar.time)
    }

    fun getCurrentDayInMillis(): Long {
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }

    fun getCurrentTimeInMillis(): Long {
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.timeInMillis
    }

    fun Int.timeHumanFormat() = toString().let {
        ("0$it").takeLast(2)
    }
}