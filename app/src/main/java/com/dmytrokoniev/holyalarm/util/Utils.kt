package com.dmytrokoniev.holyalarm.util

import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.dmytrokoniev.holyalarm.storage.SharedPreferencesAlarmStorage
import com.dmytrokoniev.holyalarm.ui.AlarmItem
import com.dmytrokoniev.holyalarm.ui.AlarmItem.Companion.toMillis

/**
 * A convenient function to show a [Toast] using any [View] and it's [Context].
 */
fun View.toast(
    text: String,
    duration: Int = LENGTH_SHORT
) = Toast.makeText(context, text, duration).show()

/**
 * A convenient function to show a [Toast] using any [AppCompatActivity] and it's
 * [android.R.id.content] if present. Toast will not be shown if the [android.R.id.content]
 * is `null`.
 */
fun AppCompatActivity.toast(
    text: String,
    duration: Int = LENGTH_SHORT
) = findViewById<View>(android.R.id.content)?.run { toast(text, duration) }

fun AlarmHelper.setAlarm(alarmItem: AlarmItem) = setAlarm(alarmItem.toMillis(), alarmItem.id)

fun AlarmHelper.cancelAlarm(alarmItem: AlarmItem) = cancelAlarm(alarmItem.id)

fun SharedPreferencesAlarmStorage.deleteItem(alarmItem: AlarmItem) = deleteItem(alarmItem.id)
