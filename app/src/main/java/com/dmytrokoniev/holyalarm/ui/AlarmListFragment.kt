package com.dmytrokoniev.holyalarm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.storage.SharedPreferencesAlarmStorage

// TODO add all LC functions with Logs
class AlarmListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_alarm_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvAlarmList = view.findViewById<RecyclerView>(R.id.rv_alarms_list)
        val btnAddAlarm = view.findViewById<Button>(R.id.btn_add_alarm)

        val rvCustomAdapter = AlarmListAdapter { isChecked: Boolean, alarmId: String ->
            (activity as? MainActivity)?.onCheckedChangeListener(isChecked, alarmId)
        }
        rvAlarmList.adapter = rvCustomAdapter
        val alarms = SharedPreferencesAlarmStorage.getItems()
        rvCustomAdapter.setAlarmList(alarms)
        btnAddAlarm.setOnClickListener {
            (activity as? MainActivity)?.onAddAlarmClick()
        }
    }
}
