package com.dmytrokoniev.holyalarm.alarmlist

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.BuildConfig
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.bus.AlarmListFragmentEvent.AddClicked
import com.dmytrokoniev.holyalarm.bus.EventBus
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.data.SortierStandart
import com.dmytrokoniev.holyalarm.data.storage.AlarmStorage
import com.dmytrokoniev.holyalarm.util.*
import com.google.android.material.snackbar.Snackbar


// TODO add all LC functions with Logs
class AlarmListFragment : Fragment(R.layout.fragment_alarm_list), IAlarmListFragment {

    private val presenter: IAlarmListPresenter = AlarmListPresenter(this)
    private lateinit var adapter: AlarmListAdapter
    private var rvAlarmList: RecyclerView? = null
    private var btnAddAlarm: Button? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvAlarmList = view.findViewById(R.id.rv_alarms_list)
        btnAddAlarm = view.findViewById(R.id.btn_add_alarm)
        btnAddAlarm?.setOnClickListener {
            if (BuildConfig.DEBUG) {
                presenter.onAddOneMinuteAlarmClicked()
            } else {
                // TODO: Move to presenter
                launchInFragmentScope {
                    EventBus.emitEvent(AddClicked)
                }
            }
        }

        adapter = AlarmListAdapter()
        val sortedAlarms = presenter.getAlarmList()
        adapter.setAlarmList(sortedAlarms)
        adapter.setLaunchInFragmentScope(::launchInFragmentScope)
        rvAlarmList?.adapter = adapter
    }

    override fun onAddAlarmToAdapter(alarmItem: AlarmItem) {
        adapter.addAlarm(alarmItem)
    }

    override fun attachTouchHelper(touchHelper: ItemTouchHelper) {
        touchHelper.attachToRecyclerView(rvAlarmList)
    }

    // TODO: Move ItemTouchHelper to separate class with lambda functions as parametrs
    override fun createTouchHelper(): ItemTouchHelper {
        val touchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val removedAlarmPosition = viewHolder.bindingAdapterPosition
                    adapter.removeAlarm(removedAlarmPosition) { alarmToRemove ->
                        presenter.onRemoveAlarm(alarmToRemove)
                        Snackbar.make(
                            viewHolder.itemView,
                            "Alarm removed",
                            Snackbar.LENGTH_LONG
                        ).setAction("UNDO") {
                            adapter.addAlarm(alarmToRemove, removedAlarmPosition)
                            presenter.onAddAlarm(alarmToRemove)
                        }.show()
                    }
                }
            })

        return touchHelper
    }
}
