package com.dmytrokoniev.holyalarm.ui

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.R

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
    }

    private fun setListeners(formattedHour: String, formattedMinute: String) {
        swchEnabled.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Toast.makeText(
                    buttonView.context,
                    "Checked time $formattedHour:$formattedMinute",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    buttonView.context,
                    "UNChecked time $formattedHour:$formattedMinute",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun Int.timeHumanFormat() = toString().let {
        ("0$it").takeLast(2)
    }
}
