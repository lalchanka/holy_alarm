package com.dmytrokoniev.holyalarm.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.storage.Storage
import com.dmytrokoniev.holyalarm.ui.AlarmSetFragment.Companion.KEY_ALARM_ID
import com.dmytrokoniev.holyalarm.util.EventBus
import com.dmytrokoniev.holyalarm.util.StopAlarmFragmentEvent
import com.dmytrokoniev.holyalarm.util.TimeUtils.timeHumanFormat
import com.dmytrokoniev.holyalarm.util.launchInFragmentScope

class StopAlarmFragment : Fragment(R.layout.fragment_stop_alarm) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnStop = view.findViewById<View>(R.id.btn_stop)
        val tvAlarmTime = view.findViewById<TextView>(R.id.tv_alarm_time)

        val alarmId = arguments?.getString(KEY_ALARM_ID)
        val alarm = Storage.getItems().find { it.id == alarmId }
        val formattedHours = alarm?.hour?.timeHumanFormat() ?: "Time"
        val formattedMinutes = alarm?.minute?.timeHumanFormat() ?: "Error $ERROR_TRIGGER_TIME"
        tvAlarmTime.text = view.context.getString(
            R.string.alarm_time,
            formattedHours,
            formattedMinutes
        )

        btnStop.setOnClickListener {
            alarmId?.let { id ->
                launchInFragmentScope {
                    EventBus.onSendEvent(StopAlarmFragmentEvent.StopClicked(id))
                }
            }
        }
    }

    companion object {
        const val ERROR_TRIGGER_TIME = "(((((:"
    }
}
