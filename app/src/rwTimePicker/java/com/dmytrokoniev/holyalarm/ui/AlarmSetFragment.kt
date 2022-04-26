package com.dmytrokoniev.holyalarm.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.util.AlarmReceiver
import ru.ifr0z.timepickercompact.TimePickerCompact
import java.text.SimpleDateFormat
import java.util.*
import android.text.format.DateUtils as DateUtils1

class AlarmSetFragment : Fragment() {

    private lateinit var btnConfirm: View
    private lateinit var btnCancel: View
    private lateinit var tpAlarmTime: TimePickerCompact
    private var alarmTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_set_alarm, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnCancel = view.findViewById<ImageButton>(R.id.btn_cancel)
        btnConfirm = view.findViewById(R.id.btn_confirm)
        tpAlarmTime = view.findViewById(R.id.tp_alarm_time)
        val alarmManager = view.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        btnConfirm.setOnClickListener {
            tpAlarmTime.run {
                val date: Date = Date()
                val calendar = Calendar.getInstance()
                calendar.time = date
                Log.d("AlarmSetFragment", "date CalendarBefore ${calendar.time}")

                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                alarmTime = calendar.timeInMillis

                btnConfirmLogs(hour, minute, calendar)
            }
            val intent = Intent(
                view.context,
                AlarmReceiver::class.java
            )
            val pendingIntent = PendingIntent.getBroadcast(
                view.context,
                12,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
            )
            val alarmClockInfo = AlarmManager.AlarmClockInfo(
                alarmTime,
                pendingIntent
            )

            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)

            val newAlarm = AlarmItem(
                id = UUID.randomUUID().toString(),
                hour = tpAlarmTime.hour,
                minute = tpAlarmTime.minute,
                is24HourView = tpAlarmTime.is24HourView,
                isEnabled = true
            )

            (requireActivity() as MainActivity).onConfirmClick(newAlarm)
        }

        btnCancel.setOnClickListener {
            (activity as? MainActivity)?.onCancelClick()
        }
    }

    private fun btnConfirmLogs(hour: Int, minute: Int, calendar: Calendar) {
        Log.d("AlarmSetFragment", "hour $hour")
        Log.d("AlarmSetFragment", "minute $minute")
        Log.d("AlarmSetFragment", "calendar.time ${calendar.time}")
        Log.d("AlarmSetFragment", "calendar.timeInMillis ${calendar.timeInMillis}")
        Log.d("AlarmSetFragment", "alarmTime + $alarmTime")
    }
}
