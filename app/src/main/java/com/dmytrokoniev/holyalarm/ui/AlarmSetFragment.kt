package com.dmytrokoniev.holyalarm.ui

import android.app.AlarmManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.TimeProvider

class AlarmSetFragment: Fragment() {

    private lateinit var btnConfirm: View
    private val timeProvider = TimeProvider()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_set_alarm, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnConfirm = view.findViewById(R.id.btn_confirm)

        val alarmManager = view.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        btnConfirm.setOnClickListener {
            val alarmClockInfo = AlarmManager.AlarmClockInfo(
                timeProvider.provide(),

            )
            alarmManager.setAlarmClock()
        }
    }
}
