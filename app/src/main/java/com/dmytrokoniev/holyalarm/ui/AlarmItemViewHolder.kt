package com.dmytrokoniev.holyalarm.ui

import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.R

class AlarmItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // TODO: 12/28/2021 findView & viewHolder

    private val alarmTime = itemView.findViewById<TextView>(R.id.tv_time)
    private val dayPart = itemView.findViewById<TextView>(R.id.tv_day_part)
    val enabled = itemView.findViewById<Switch>(R.id.swch_enabled)

    fun bind(alarmItem: AlarmItem) {
                alarmTime.text = "${alarmItem.hour}:${alarmItem.minute}"
                dayPart.text = if (alarmItem.is24HourView) "AM" else "PM"
                enabled.isChecked = if (alarmItem.isEnabled) true else false
    }
}
