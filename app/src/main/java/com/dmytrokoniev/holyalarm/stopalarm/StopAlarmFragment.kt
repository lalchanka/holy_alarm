package com.dmytrokoniev.holyalarm.stopalarm

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.ui.AlarmSetFragment.Companion.KEY_ALARM_ID
import com.dmytrokoniev.holyalarm.util.TimeUtils.timeHumanFormat


class StopAlarmFragment : Fragment(R.layout.fragment_stop_alarm), IStopAlarmFragment {

    private val stopAlarmPresenter: IStopAlarmPresenter = StopAlarmPresenter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnStop = view.findViewById<View>(R.id.btn_stop)
        val tvAlarmTime = view.findViewById<TextView>(R.id.tv_alarm_time)
        val alarmId = arguments?.getString(KEY_ALARM_ID)

        context?.let { context ->
            alarmId?.let { stopAlarmPresenter.initialize(context, this.lifecycleScope, alarmId) }
        }
        alarmId?.let {
            val alarmItem = stopAlarmPresenter.getAlarmItem()
            val formattedHours = alarmItem?.hour?.timeHumanFormat() ?: "Time"
            val formattedMinutes = alarmItem?.minute?.timeHumanFormat() ?: "Error $ERROR_TRIGGER_TIME"

            tvAlarmTime.text = view.context.getString(
                R.string.alarm_time,
                formattedHours,
                formattedMinutes
            )
        }
        btnStop.setOnClickListener {
            alarmId?.let {
                stopAlarmPresenter.onStopAlarmClick()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAlarmPresenter.dispose()
    }

    companion object {
        const val ERROR_TRIGGER_TIME = "(((((:"
    }
}
