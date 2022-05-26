package com.dmytrokoniev.holyalarm.data

import android.os.Parcelable
import androidx.annotation.IntRange
import kotlinx.parcelize.Parcelize
import java.util.Calendar
import java.util.Date

@Parcelize
data class AlarmItem(
    val id: String,
    @IntRange(from = 0, to = 23) val hour: Int,
    @IntRange(from = 0, to = 59) val minute: Int,
    val is24HourView: Boolean = true,
    val isEnabled: Boolean = true
) : Parcelable {

    companion object {
        fun AlarmItem.toMillis(): Long {
            val date = Date()
            val calendar = Calendar.getInstance()
            calendar.time = date

            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            return calendar.timeInMillis
        }
    }
}