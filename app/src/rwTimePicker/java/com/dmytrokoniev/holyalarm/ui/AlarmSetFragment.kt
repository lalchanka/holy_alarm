package com.dmytrokoniev.holyalarm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.util.AlarmTimeBus
import kotlin.random.Random

class AlarmSetFragment : Fragment(R.layout.fragment_set_alarm) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tpAlarmTime = view.findViewById<TimePicker>(R.id.tp_alarm_time)
        val alarmId = Random.nextInt()

        tpAlarmTime.setOnTimeChangedListener { _, hourOfDay, minute ->
            val newAlarm = AlarmItem(
                id = alarmId.toString(),
                hour = hourOfDay,
                minute = minute,
                is24HourView = tpAlarmTime.is24HourView,
                isEnabled = true
            )

            AlarmTimeBus.lastAlarmTimeSet = newAlarm
        }
    }

    companion object {
        const val KEY_ALARM_ID = "TRIGGER_ALARM_TIME_KEY"
    }
}
