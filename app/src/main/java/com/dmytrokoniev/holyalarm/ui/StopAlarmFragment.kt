package com.dmytrokoniev.holyalarm.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.bus.AlarmItemBus
import com.dmytrokoniev.holyalarm.bus.EventBus
import com.dmytrokoniev.holyalarm.bus.StopAlarmFragmentEvent.StopClicked
import com.dmytrokoniev.holyalarm.storage.Storage
import com.dmytrokoniev.holyalarm.ui.AlarmSetFragment.Companion.KEY_ALARM_ID
import com.dmytrokoniev.holyalarm.util.TimeUtils.timeHumanFormat
import com.dmytrokoniev.holyalarm.util.launchInFragmentScope

class StopAlarmFragment : Fragment(R.layout.fragment_stop_alarm) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnStop = view.findViewById<View>(R.id.btn_stop)
        val tvAlarmTime = view.findViewById<TextView>(R.id.tv_alarm_time)

        val alarmId = arguments?.getString(KEY_ALARM_ID)
        val alarmItem = Storage.getItems().find { it.id == alarmId }
        val formattedHours = alarmItem?.hour?.timeHumanFormat() ?: "Time"
        val formattedMinutes = alarmItem?.minute?.timeHumanFormat() ?: "Error $ERROR_TRIGGER_TIME"
        tvAlarmTime.text = view.context.getString(
            R.string.alarm_time,
            formattedHours,
            formattedMinutes
        )

        btnStop.setOnClickListener {
            alarmItem?.let { alarmItemNotNull ->
                launchInFragmentScope {
                    AlarmItemBus.onSendAlarmItem(alarmItemNotNull)
                    EventBus.onSendEvent(StopClicked)
                }
            }
        }
    }

    companion object {
        const val ERROR_TRIGGER_TIME = "(((((:"
    }
}
