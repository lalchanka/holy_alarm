package com.dmytrokoniev.holyalarm.alarmlist

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.BuildConfig
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.util.AlarmListOnDeleteTouchHelper
import com.dmytrokoniev.holyalarm.util.addAlarm
import com.dmytrokoniev.holyalarm.util.launchInFragmentScope
import com.google.android.material.snackbar.Snackbar

class AlarmListFragment : Fragment(R.layout.fragment_alarm_list), IAlarmListFragment {

    private val presenter: IAlarmListPresenter = AlarmListPresenter(this)
    private lateinit var adapter: AlarmListAdapter
    private var rvAlarmList: RecyclerView? = null
    private var btnAddAlarm: Button? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.initialize(lifecycleScope)
        rvAlarmList = view.findViewById(R.id.rv_alarms_list)
        btnAddAlarm = view.findViewById(R.id.btn_add_alarm)
        btnAddAlarm?.setOnClickListener {
            if (BuildConfig.BUILD_TYPE == "debug") {
                presenter.onAddOneMinuteAlarmClicked()
            } else {
                presenter.onAddAlarmClicked()
            }
        }

        adapter = AlarmListAdapter()
        val sortedAlarms = presenter.getAlarmList()
        adapter.setAlarmList(sortedAlarms)
        adapter.setLaunchInFragmentScope(::launchInFragmentScope)
        rvAlarmList?.adapter = adapter
        createAttachTouchHelper()
    }

    override fun onAddAlarmToAdapter(alarmItem: AlarmItem) {
        adapter.addAlarm(alarmItem)
    }

    override fun createAttachTouchHelper() {
        val touchHelper = ItemTouchHelper(
            AlarmListOnDeleteTouchHelper { viewHolder, _ ->
                val removedAlarmPosition = viewHolder.bindingAdapterPosition
                adapter.removeAlarm(removedAlarmPosition) { alarmToRemove ->
                    presenter.removeAlarm(alarmToRemove)
                    Snackbar.make(
                        viewHolder.itemView,
                        "Alarm removed",
                        Snackbar.LENGTH_LONG
                    ).setAction("UNDO") {
                        adapter.addAlarm(alarmToRemove, removedAlarmPosition)
                        presenter.addAlarm(alarmToRemove)
                    }.show()
                }
            })

        touchHelper.attachToRecyclerView(rvAlarmList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.dispose()
    }
}
