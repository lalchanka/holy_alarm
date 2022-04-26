package com.dmytrokoniev.holyalarm.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.util.AlarmReceiver
import com.dmytrokoniev.holyalarm.util.TimeUtils.timeHumanFormat

// TODO: 12/28/2021 findView & viewHolder
class AlarmItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvAlarmTime = itemView.findViewById<TextView>(R.id.tv_alarm_time)
    private val swchEnabled = itemView.findViewById<SwitchCompat>(R.id.swch_enabled)

    fun bind(alarmItem: AlarmItem) {
        val context = tvAlarmTime.context
        val formattedHour = alarmItem.hour.timeHumanFormat()
        val formattedMinute = alarmItem.minute.timeHumanFormat()
        val alarmTimeText = context.getString(
            R.string.alarm_time,
            formattedHour,
            formattedMinute
        )
        tvAlarmTime.text = alarmTimeText
        swchEnabled.isChecked = alarmItem.isEnabled

        setListeners(formattedHour, formattedMinute)
        // TODO: danylo.oliinyk@pluto.tv 26.04.2022 add swipe to delete or long press
    }

    // TODO: danylo.oliinyk@pluto.tv 26.04.2022 move to AlarmHelper
    private fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(
            context,
            AlarmReceiver::class.java
        )
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            12,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun setListeners(formattedHour: String, formattedMinute: String) {
        swchEnabled.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Toast.makeText(
                    buttonView.context,
                    "Alarm set for: $formattedHour:$formattedMinute",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    buttonView.context,
                    "Cancelled alarm: $formattedHour:$formattedMinute",
                    Toast.LENGTH_SHORT
                ).show()
                cancelAlarm(buttonView.context)
            }
        }
    }
}
