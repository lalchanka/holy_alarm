package com.dmytrokoniev.holyalarm.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.navigate.FragmentNavigator

class AlarmListFragment : Fragment() {

    // TODO add all LC functions with Logs

//    val navigator: FragmentNavigator


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_alarm_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemList = listOf(
            AlarmItem("10:00", "AM"),
            AlarmItem("08:00", "AM"),
            AlarmItem("03:00", "PM")
        )
        val rvCustomAdapter = AlarmListAdapter()
        val rvAlarmList = view.findViewById<RecyclerView>(R.id.rv_alarms_list)
        val btnAddAlarm = view.findViewById<Button>(R.id.btn_add_alarm)
        rvAlarmList.adapter = rvCustomAdapter
        rvCustomAdapter.setAlarmList(itemList)

        btnAddAlarm.setOnClickListener {
            (activity as? MainActivity)?.loadAlarmSetFragment()
//            navigator.someFunction()

            rvCustomAdapter.addAlarm(
                AlarmItem(
                    "${(12..24).random()}:${(0..9).random()}${(0..9).random()}",
                    "PM"
                )
            )
            Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show()
        }


//        Handler().postDelayed(
//            {
//                rvCustomAdapter.setAlarmList(itemList)
//            },
//            3000
//        )
//        val itemList = mutableListOf<String>("val1", "val2", "val3")
//        val arrAdapter = ArrayAdapter(requireContext(), R.layout.item_alarm, R.id.view_1, itemList)
//        val rvAlarmsList = view.findViewById<ListView>(R.id.rv_alarms_list)
//
//        rvAlarmsList.adapter = arrAdapter
//
//        Log.d(AlarmListFragment::class.simpleName,"On_VIew_Created")
    }
}
