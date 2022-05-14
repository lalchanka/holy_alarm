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
import com.google.android.material.snackbar.Snackbar


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

        val rvCustomAdapter = AlarmListAdapter()
        rvAlarmList.adapter = rvCustomAdapter
        val alarms = SharedPreferencesAlarmStorage.getItems()
        rvCustomAdapter.setAlarmList(alarms)
        rvCustomAdapter.setCheckedChangeListener { isChecked: Boolean, alarmItem: AlarmItem ->
            (activity as? MainActivity)?.onCheckedChangeListener(isChecked, alarmItem)
        }
        btnAddAlarm.setOnClickListener {
            (activity as? MainActivity)?.onAddAlarmClick()
        }

        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val removedPosition = viewHolder.bindingAdapterPosition
                rvCustomAdapter.removeAlarm(removedPosition)

                Snackbar.make(
                    viewHolder.itemView,
                    "Alarm removed",
                    Snackbar.LENGTH_LONG).setAction("UNDO") {
                    rvCustomAdapter.getAlarm(removedPosition)
                    rvCustomAdapter.notifyItemInserted(removedPosition)

                }.show()
                // below line is to display our snackbar with action.
//                Snackbar.make(courseRV, deletedCourse.getTitle(), Snackbar.LENGTH_LONG)
//                    .setAction("Undo",
//                        View.OnClickListener { // adding on click listener to our action of snack bar.
//                            // below line is to add our item to array list with a position.
//                            recyclerDataArrayList.add(position, deletedCourse)
//
//                            // below line is to notify item is
//                            // added to our adapter class.
//                            recyclerViewAdapter.notifyItemInserted(position)
//                        }).show()
            }
        })
        touchHelper.attachToRecyclerView(rvAlarmList)
    }
}
