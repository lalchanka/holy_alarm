package com.dmytrokoniev.holyalarm.ui

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.BuildConfig
import com.dmytrokoniev.holyalarm.R

class AlarmSetFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_set_alarm, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCancel = view.findViewById<ImageButton>(R.id.btn_cancel)
        val btnConfirm = view.findViewById<ImageButton>(R.id.btn_confirm)

        btnCancel.setOnClickListener {
            (activity as? MainActivity)?.loadAlarmListFragment()
        }

        btnConfirm.setOnClickListener {
            // 05.01.2022 dmytrokoniev@gmail.com TODO: <text of todo>
        }
    }
}
