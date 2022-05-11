package com.dmytrokoniev.holyalarm.util

import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.dmytrokoniev.holyalarm.ui.AlarmItem
import com.dmytrokoniev.holyalarm.ui.AlarmItem.Companion.toMillis

/**
 * A convenient function to show a [Toast] using any [View] and it's [Context].
 */
fun View.toast(
    text: String,
    duration: Int = LENGTH_SHORT
) = Toast.makeText(context, text, duration).show()

//fun Activity.toast(
//    text: String,
//    duration: Int = LENGTH_SHORT
//) = view

fun AlarmHelper.setAlarm(alarmItem: AlarmItem) = setAlarm(alarmItem.toMillis(), alarmItem.id)
