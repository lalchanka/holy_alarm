package com.dmytrokoniev.holyalarm.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.util.AlarmReceiver

class StopAlarmFragment : Fragment(R.layout.fragment_stop_alarm) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnStop = view.findViewById<View>(R.id.btn_stop)
        val tvAlarmTime = view.findViewById<TextView>(R.id.tv_alarm_time)

        val alarmTime = arguments?.getString(ALARM_TRIGGER_TIME) ?: ERROR_TRIGGER_TIME
        tvAlarmTime.text = alarmTime

        btnStop.setOnClickListener {
            // TODO: danylo.oliinyk@pluto.tv 26.04.2022 move to a AlarmHelper class
            val alarmManager = view.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
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
            alarmManager.cancel(pendingIntent)
        }
    }

    companion object {
        const val ALARM_TRIGGER_TIME = "ALARM_TRIGGER_TIME"
        const val ERROR_TRIGGER_TIME = "(((((:"
    }
}
