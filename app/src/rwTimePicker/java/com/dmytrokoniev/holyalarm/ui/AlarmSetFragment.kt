package com.dmytrokoniev.holyalarm.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.util.AlarmHelper
import com.dmytrokoniev.holyalarm.util.IToolbar
import ru.ifr0z.timepickercompact.TimePickerCompact
import java.util.Calendar
import java.util.Date
import kotlin.random.Random

class AlarmSetFragment : Fragment() {

    private var alarmTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_set_alarm, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnCancel = view.findViewById<ImageButton>(R.id.btn_cancel)
        val btnConfirm = view.findViewById<View>(R.id.btn_confirm)
        val tpAlarmTime = view.findViewById<TimePickerCompact>(R.id.tp_alarm_time)
        val toolbar = activity as IToolbar

        btnConfirm.setOnClickListener {
            tpAlarmTime.run {
                val date = Date()
                val calendar = Calendar.getInstance()
                calendar.time = date
                Log.d("AlarmSetFragment", "date CalendarBefore ${calendar.time}")

                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                alarmTime = calendar.timeInMillis

                printLogs(hour, minute, calendar)
            }
            val alarmId = Random.nextInt()
            val newAlarm = AlarmItem(
                id = alarmId.toString(),
                hour = tpAlarmTime.hour,
                minute = tpAlarmTime.minute,
                is24HourView = tpAlarmTime.is24HourView,
                isEnabled = true
            )
            AlarmHelper.setAlarm(alarmTime, alarmId)
            (requireActivity() as MainActivity).onConfirmClick(newAlarm)
        }

        btnCancel.setOnClickListener {
            (activity as? MainActivity)?.onCancelClick()
        }
    }

    private fun printLogs(hour: Int, minute: Int, calendar: Calendar) {
        Log.d("AlarmSetFragment", "hour $hour")
        Log.d("AlarmSetFragment", "minute $minute")
        Log.d("AlarmSetFragment", "calendar.time ${calendar.time}")
        Log.d("AlarmSetFragment", "calendar.timeInMillis ${calendar.timeInMillis}")
        Log.d("AlarmSetFragment", "alarmTime + $alarmTime")
    }

    companion object {
        const val KEY_ALARM_ID = "TRIGGER_ALARM_TIME_KEY"
    }
}
