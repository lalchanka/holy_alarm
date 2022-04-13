package com.dmytrokoniev.holyalarm.ui

import android.media.Image
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.BuildConfig
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.util.AlarmReceiver
import ru.ifr0z.timepickercompact.TimePickerCompact

class AlarmSetFragment : Fragment() {

    private lateinit var btnConfirm: View
    private lateinit var tpAlarmTime: TimePickerCompact
    private var alarmTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_set_alarm, container, false)

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Change declaration of btnCancel as Dan made it
        val btnCancel = view.findViewById<ImageButton>(R.id.btn_cancel)
        btnConfirm = view.findViewById(R.id.btn_confirm)
        tpAlarmTime = view.findViewById(R.id.tp_alarm_time)

        tpAlarmTime.run {
            val hoursInMillis = (hour * 60 * 60 * 1000).toLong()
            val minutesInMillis = (minute * 60 * 1000).toLong()
            alarmTime = System.currentTimeMillis() + hoursInMillis + minutesInMillis
        }

        val alarmManager = view.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        btnConfirm.setOnClickListener {

            val activityIntent = Intent(
                view.context,
                MainActivity::class.java
            )
            val activityPendingIntent = PendingIntent.getActivity(
                view.context,
                7,
                activityIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )

            val intentBroadcast = Intent(
                view.context,
                AlarmReceiver::class.java
            )
            val pendingIntentBroadcast = PendingIntent.getBroadcast(
                view.context,
                12,
                intentBroadcast,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            val alarmClockInfo = AlarmManager.AlarmClockInfo(
                alarmTime,
                activityPendingIntent
            )

            alarmManager.setAlarmClock(alarmClockInfo, pendingIntentBroadcast)

            (requireActivity() as MainActivity).loadAlarmListFragment()
        }

        btnCancel.setOnClickListener {
            (activity as? MainActivity)?.loadAlarmListFragment()
        }
    }
}
