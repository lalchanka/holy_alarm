package com.dmytrokoniev.holyalarm.ui

import android.media.Image
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.BuildConfig
import com.dmytrokoniev.holyalarm.R

class AlarmSetFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_set_alarm, container, false)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCancel = view.findViewById<ImageButton>(R.id.btn_cancel)
        val btnConfirm = view.findViewById<ImageButton>(R.id.btn_confirm)

        btnCancel.setOnClickListener {
            (activity as? MainActivity)?.onCancelClick()
        }

        btnConfirm.setOnClickListener {
            val timePicker = view.findViewById<TimePicker>(R.id.tp_alarm_time)
            val newAlarm = AlarmItem(
                id = 1,
                hour = timePicker.hour,
                minute = timePicker.minute,
                is24HourView = timePicker.is24HourView
            )

            (activity as? MainActivity)?.onConfirmClick(newAlarm)
        }
    }
}
