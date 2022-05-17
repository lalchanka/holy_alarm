package com.dmytrokoniev.holyalarm.ui

import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.bus.AlarmItemBus
import com.dmytrokoniev.holyalarm.util.launchInFragmentScope

abstract class AlarmSetFragment : Fragment(R.layout.fragment_set_alarm) {

    protected var tpAlarmTime: TimePicker? = null
    protected abstract val alarmIdProvider: () -> String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tpAlarmTime = view.findViewById(R.id.tp_alarm_time)
        tpAlarmTime?.setIs24HourView(IS_24_FORMAT_ENABLED)
        tpAlarmTime?.setOnTimeChangedListener { _, hourOfDay, minute ->
            val newAlarm = AlarmItem(
                id = alarmIdProvider(),
                hour = hourOfDay,
                minute = minute,
                is24HourView = IS_24_FORMAT_ENABLED,
                isEnabled = true
            )
            launchInFragmentScope {
                AlarmItemBus.onSendAlarmItem(newAlarm)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tpAlarmTime = null
    }

    companion object {
        const val KEY_ALARM_ID = "TRIGGER_ALARM_TIME_KEY"
        const val IS_24_FORMAT_ENABLED = true
    }
}
