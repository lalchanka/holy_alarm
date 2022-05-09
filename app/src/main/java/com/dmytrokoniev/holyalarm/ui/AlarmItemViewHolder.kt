package com.dmytrokoniev.holyalarm.ui

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.storage.SharedPreferencesAlarmStorage
import com.dmytrokoniev.holyalarm.storage.updateItemIsEnabled
import com.dmytrokoniev.holyalarm.ui.AlarmItem.Companion.toMillis
import com.dmytrokoniev.holyalarm.util.AlarmHelper
import com.dmytrokoniev.holyalarm.util.TimeUtils.timeHumanFormat
import com.dmytrokoniev.holyalarm.util.toast

// TODO: 12/28/2021 findView & viewHolder
// TODO: danylo.oliinyk@pluto.tv 26.04.2022 add swipe to delete or long press
class AlarmItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvAlarmTime = itemView.findViewById<TextView>(R.id.tv_alarm_time)
    private val swchEnabled = itemView.findViewById<SwitchCompat>(R.id.swch_enabled)

    fun bind(alarmItem: AlarmItem) {
        val context = tvAlarmTime.context
        val (formattedHour, formattedMinute) = alarmItem.run {
            hour.timeHumanFormat() to minute.timeHumanFormat()
        }
        val alarmTimeText = context.getString(
            R.string.alarm_time,
            formattedHour,
            formattedMinute
        )
        tvAlarmTime.text = alarmTimeText
        swchEnabled.isChecked = alarmItem.isEnabled

        setListeners(formattedHour, formattedMinute, alarmItem.id)
    }

    private fun setListeners(formattedHour: String, formattedMinute: String, alarmId: String) {
        swchEnabled.setOnCheckedChangeListener { btnView, isChecked ->
            if (isChecked) {
                val alarmItem = SharedPreferencesAlarmStorage.getItem(alarmId)
                    ?: return@setOnCheckedChangeListener
                AlarmHelper.setAlarm(alarmItem.toMillis(), alarmId)
                SharedPreferencesAlarmStorage.updateItemIsEnabled(alarmId, isEnabled = true)
                btnView.toast("Alarm set for: $formattedHour:$formattedMinute")
            } else {
                AlarmHelper.cancelAlarm(alarmId)
                SharedPreferencesAlarmStorage.updateItemIsEnabled(alarmId, isEnabled = false)
                btnView.toast("Cancelled alarm: $formattedHour:$formattedMinute")
            }
        }
    }
}
