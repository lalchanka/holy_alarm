package com.dmytrokoniev.holyalarm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.R

class AlarmListFragment : Fragment() {

    // TODO add all LC functions with Logs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_alarm_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alarmInMemoryStorage = (activity as? MainActivity)?.alarmStorage
        //val newAlarm = arguments?.getSerializable("newAlarmItem") as? AlarmItem
        val rvCustomAdapter = AlarmListAdapter()
        val rvAlarmList = view.findViewById<RecyclerView>(R.id.rv_alarms_list)
        val btnAddAlarm = view.findViewById<Button>(R.id.btn_add_alarm)

        rvAlarmList.adapter = rvCustomAdapter
        alarmInMemoryStorage?.let {
            rvCustomAdapter.setAlarmList(it.getItems())
        }
//        newAlarm?.let {
//            rvCustomAdapter.addAlarm(it)
//        }

        btnAddAlarm.setOnClickListener {
            (activity as? MainActivity)?.onAddAlarmClick()
        }
    }
}
