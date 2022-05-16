package com.dmytrokoniev.holyalarm.ui

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.util.TimeUtils.timeHumanFormat

// TODO: 12/28/2021 findView & viewHolder
// TODO: danylo.oliinyk@pluto.tv 26.04.2022 add swipe to delete or long press
class AlarmItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvAlarmTime = itemView.findViewById<TextView>(R.id.tv_alarm_time)
    private val swchEnabled = itemView.findViewById<SwitchCompat>(R.id.swch_enabled)

    fun bind(alarmItem: AlarmItem, checkedChangeListener: ((Boolean, AlarmItem) -> Unit)?) {
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
        swchEnabled.setOnCheckedChangeListener { _, isChecked ->
            checkedChangeListener?.invoke(isChecked, alarmItem)
        }
    }
}
