package com.dmytrokoniev.holyalarm.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

object TimeUtils {

    fun millisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS", Locale.getDefault())
        val timeZone = TimeZone.getDefault()
        timeZone.rawOffset
        formatter.timeZone = TimeZone.getDefault()
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return formatter.format(calendar.time)
    }

    fun Int.timeHumanFormat() = toString().let {
        ("0$it").takeLast(2)
    }
}