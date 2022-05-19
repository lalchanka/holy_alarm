package com.dmytrokoniev.holyalarm.ui

import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.bus.AlarmListFragmentEvent.AddClicked
import com.dmytrokoniev.holyalarm.bus.EventBus
import com.dmytrokoniev.holyalarm.storage.SharedPreferencesAlarmStorage
import com.dmytrokoniev.holyalarm.storage.Storage
import com.dmytrokoniev.holyalarm.util.*
import com.google.android.material.snackbar.Snackbar


// TODO add all LC functions with Logs
class AlarmListFragment : Fragment(R.layout.fragment_alarm_list) {

    private var rvAdapter: AlarmListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvAlarmList = view.findViewById<RecyclerView>(R.id.rv_alarms_list)
        val btnAddAlarm = view.findViewById<Button>(R.id.btn_add_alarm)
        val btnPlayRingtone = view.findViewById<Button>(R.id.btn_play_ringtone)

        rvAdapter = AlarmListAdapter()
        val alarms = Storage.getItems()
        rvAdapter?.setAlarmList(alarms)
        rvAdapter?.setLaunchInFragmentScope(::launchInFragmentScope)
        rvAlarmList.adapter = rvAdapter

        btnAddAlarm.setOnClickListener {
            launchInFragmentScope {
                EventBus.emitEvent(AddClicked)
            }
        }

        btnPlayRingtone.setOnClickListener {
            val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val r = RingtoneManager.getRingtone(context, notification)
            r.play()
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
                        AlarmManagerHelper.cancelAlarm(removedAlarm)
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
                            AlarmManagerHelper.setAlarm(removedAlarm)
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
