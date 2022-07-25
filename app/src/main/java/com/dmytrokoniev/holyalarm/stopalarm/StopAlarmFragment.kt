package com.dmytrokoniev.holyalarm.stopalarm

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.util.AlarmManagerHelper.KEY_ALARM_ID
import com.dmytrokoniev.holyalarm.util.TimeUtils.timeHumanFormat


class StopAlarmFragment : Fragment(R.layout.fragment_stop_alarm), IStopAlarmFragment {

    private var stopAlarmPresenter: IStopAlarmPresenter? = null
    private var btnStop: View? = null
    private var tvAlarmTime: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnStop = view.findViewById(R.id.btn_stop)
        tvAlarmTime = view.findViewById(R.id.tv_alarm_time)
        val alarmId = arguments?.getString(KEY_ALARM_ID)
        stopAlarmPresenter = StopAlarmPresenter(
            this,
            view.context,
            this.lifecycleScope
        )
        stopAlarmPresenter?.initialize()
        stopAlarmPresenter?.validateData(alarmId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAlarmPresenter?.dispose()
    }

    override fun onShowSuccess(alarmItem: AlarmItem?) {
        val formattedHours = alarmItem?.hour?.timeHumanFormat() ?: "Time"
        val formattedMinutes = alarmItem?.minute?.timeHumanFormat() ?: "Error $ERROR_TRIGGER_TIME"

        stopAlarmPresenter?.playRingtone(alarmItem)
        tvAlarmTime?.text = getString(
            R.string.alarm_time,
            formattedHours,
            formattedMinutes
        )
        btnStop?.setOnClickListener {
            stopAlarmPresenter?.onStopAlarmClick(alarmItem)
        }
    }

    override fun onShowError() {
        TODO("Not yet implemented")
    }

    companion object {
        const val ERROR_TRIGGER_TIME = "(((((:"
    }
}
