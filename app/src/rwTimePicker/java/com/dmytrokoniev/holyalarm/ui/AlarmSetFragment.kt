package com.dmytrokoniev.holyalarm.ui

import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.bus.AlarmItemBus
import com.dmytrokoniev.holyalarm.util.launchInFragmentScope

abstract class AlarmSetFragment : Fragment(R.layout.rw_fragment_set_alarm) {

    protected var tpAlarmTime: TimePicker? = null
    protected abstract val alarmIdProvider: () -> String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        const val KEY_ALARM_ID = "TRIGGER_ALARM_TIME_KEY"
    }
}
