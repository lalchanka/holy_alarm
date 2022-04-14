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
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class AlarmSetFragment : Fragment() {

    private lateinit var btnConfirm: View
    private lateinit var tpAlarmTime: TimePickerCompact
    private var alarmTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_set_alarm, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Change declaration of btnCancel as Dan made it
        val btnCancel = view.findViewById<ImageButton>(R.id.btn_cancel)
        btnConfirm = view.findViewById(R.id.btn_confirm)
        tpAlarmTime = view.findViewById(R.id.tp_alarm_time)

        val twentyMinutes = 20L * 60L * 1000L
//        Log.d("AlarmSetFragment", "twentyMinutes + ${millisToDate(twentyMinutes)}")

        val alarmManager = view.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        btnConfirm.setOnClickListener {
            tpAlarmTime.run {
//                val hoursInMillis = (hour * 60 * 60 * 1000).toLong()
//                val minutesInMillis = (minute * 60 * 1000).toLong()
//                val roundedCurrentTime = (System.currentTimeMillis() / 60000) * 60000
//                alarmTime = 1649915400000 // It was 8:50 AM by current time = 5:50
                alarmTime = 1649916540000 // It was 8:50 AM by current time = 5:50
//                alarmTime = roundedCurrentTime + hoursInMillis + minutesInMillis

//                Log.d("AlarmSetFragment", "hour $hour")
//                Log.d("AlarmSetFragment", "minute $minute")
//
//                Log.d("AlarmSetFragment", "System.currentTimeMillis() + ${millisToDate(System.currentTimeMillis())}")
//                Log.d("AlarmSetFragment", "hoursInMillis + ${millisToDate(hoursInMillis)}")
//                Log.d("AlarmSetFragment", "minutesInMillis + ${millisToDate(minutesInMillis)}")
//                Log.d("AlarmSetFragment", "roundedCurrentTime + ${millisToDate(roundedCurrentTime)}")
//                Log.d("AlarmSetFragment", "alarmTime + ${millisToDate(alarmTime)}")
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
                id = 1,
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

    private fun millisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS", Locale.getDefault())
        val timeZone = TimeZone.getDefault()
        timeZone.rawOffset
        formatter.timeZone = TimeZone.getDefault()
//        Log.d("AlarmSetFragment", "TimeZone: ${TimeZone.getDefault().displayName}")
//        Log.d("AlarmSetFragment", "TimeZone offset: ${timeZone.rawOffset}")

        val calendar: Calendar = Calendar.getInstance()
//        val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = millis
        return formatter.format(calendar.time)
    }
}
