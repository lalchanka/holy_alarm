package com.dmytrokoniev.holyalarm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.storage.SharedPreferencesAlarmStorage
import com.dmytrokoniev.holyalarm.storage.Storage
import com.dmytrokoniev.holyalarm.util.*
import com.dmytrokoniev.holyalarm.util.AlarmListFragmentEvent.AddClicked
import com.google.android.material.snackbar.Snackbar


// TODO add all LC functions with Logs
class AlarmListFragment : Fragment() {

    private var rvAdapter: AlarmListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_alarm_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvAlarmList = view.findViewById<RecyclerView>(R.id.rv_alarms_list)
        val btnAddAlarm = view.findViewById<Button>(R.id.btn_add_alarm)

        rvAdapter = AlarmListAdapter()
        val alarms = Storage.getItems()
        rvAdapter?.setAlarmList(alarms)
        rvAdapter?.setLaunchInFragmentScope(::launchInFragmentScope)
        rvAlarmList.adapter = rvAdapter

        btnAddAlarm.setOnClickListener {
            launchInFragmentScope {
                EventBus.onSendEvent(AddClicked)
            }
        }

        val touchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val removedAlarmPosition = viewHolder.bindingAdapterPosition
                    val removedAlarm = rvAdapter?.getAlarm(removedAlarmPosition)
                    rvAdapter?.removeAlarm(removedAlarmPosition)
                    rvAdapter?.notifyItemRemoved(removedAlarmPosition)
                    if (removedAlarm != null) {
                        SharedPreferencesAlarmStorage.deleteItem(removedAlarm)
                        AlarmHelper.cancelAlarm(removedAlarm)
                    }

                    Snackbar.make(
                        viewHolder.itemView,
                        "Alarm removed",
                        Snackbar.LENGTH_LONG
                    ).setAction("UNDO") {
                        if (removedAlarm != null) {
                            rvAdapter?.addAlarm(removedAlarmPosition, removedAlarm)
                            rvAdapter?.notifyItemInserted(removedAlarmPosition)
                            SharedPreferencesAlarmStorage.addItem(removedAlarm)
                            AlarmHelper.setAlarm(removedAlarm)
                        }
                    }.show()
                }
            })
        touchHelper.attachToRecyclerView(rvAlarmList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rvAdapter = null
    }
}
