package com.dmytrokoniev.holyalarm.data

import android.os.Parcelable
import androidx.annotation.IntRange
import com.dmytrokoniev.holyalarm.util.TimeUtils
import com.dmytrokoniev.holyalarm.util.TimeUtils.timeHumanFormat
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
        fun AlarmItem.timeToMillis(): Long =
            hour * TimeUtils.ONE_HOUR_IN_MILLIS +
                    minute * TimeUtils.ONE_MIN_IN_MILLIS

        fun AlarmItem.timeToHumanFormat(): String =
            hour.timeHumanFormat() + ":" + minute.timeHumanFormat()
    }
}
