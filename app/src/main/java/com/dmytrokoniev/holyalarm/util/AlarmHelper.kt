package com.dmytrokoniev.holyalarm.util

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.dmytrokoniev.holyalarm.ui.AlarmSetFragment.Companion.KEY_ALARM_ID

/**
 * [AlarmHelper] encapsulates tasks related to Alarm and creates a convenient API to simplify
 * work with AlarmManager. It is a singleton, hence initialize it once before the usage and
 * dispose it when you don't need it anymore in scope of the app lifecycle.
 */
@SuppressLint("StaticFieldLeak")
object AlarmHelper {

    private var context: Context? = null
    private var alarmManager: AlarmManager? = null

    /**
     * Initializes the [AlarmHelper] object. Call this function in prior to any other function call
     * on the [AlarmHelper] object.
     */
    fun initialize(context: Context) {
        this.context = context
        this.alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
    }

    fun setAlarm(time: Long, id: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(KEY_ALARM_ID, id.toString())
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )
        val alarmClockInfo = AlarmManager.AlarmClockInfo(
            time,
            pendingIntent
        )
        alarmManager?.setAlarmClock(alarmClockInfo, pendingIntent)
    }

    fun cancelAlarm(id: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )
        alarmManager?.cancel(pendingIntent)
    }

    /**
     * Disposes the [AlarmHelper] object. After calling this function the object is not usable
     * anymore.
     */
    fun dispose() {
        context = null
        alarmManager = null
    }
}
