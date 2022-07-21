package com.dmytrokoniev.holyalarm.alarmlist

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.data.SortierStandart
import com.dmytrokoniev.holyalarm.data.storage.AlarmStorage
import com.dmytrokoniev.holyalarm.util.AlarmManagerHelper
import com.dmytrokoniev.holyalarm.util.addAlarm
import com.dmytrokoniev.holyalarm.util.setAlarm
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.util.*

class AlarmListPresenter(
    private val view: IAlarmListFragment
) : IAlarmListPresenter {

    private lateinit var adapter: AlarmListAdapter
    private var touchHelper: ItemTouchHelper? = null

    override fun initialize(coroutineScope: CoroutineScope) {
        val alarms = AlarmStorage.getItems()
        val sortedAlarms = SortierStandart.sortAscending(alarms).toList()
        adapter = AlarmListAdapter()
        adapter.setAlarmList(sortedAlarms)
        adapter.setLaunchInFragmentScope(::coroutineScope)
        view.setAlarmListAdapter(adapter)

        createTouchHelper()
        view.attachTouchHelper(touchHelper)
    }

    override fun onAddOneMinuteAlarmClicked() {
        val exactTime = Calendar.getInstance()
        val alarmItem = AlarmItem(
            id = Random().nextInt().toString(),
            hour = exactTime.get(Calendar.HOUR_OF_DAY),
            minute = exactTime.get(Calendar.MINUTE) + 1,
            is24HourView = true,
            isEnabled = true
        )
        AlarmStorage.addItem(alarmItem)
        AlarmManagerHelper.setAlarm(alarmItem)
        adapter.addAlarm(alarmItem)
        adapter.notifyDataSetChanged()
    }

    override fun createTouchHelper() {
        touchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val removedAlarmPosition = viewHolder.bindingAdapterPosition
                    adapter.removeAlarm(removedAlarmPosition) { alarmToRemove ->
                        AlarmStorage.deleteItem(alarmToRemove)
                        AlarmManagerHelper.cancelAlarm(alarmToRemove.id)

                        Snackbar.make(
                            viewHolder.itemView,
                            "Alarm removed",
                            Snackbar.LENGTH_LONG
                        ).setAction("UNDO") {
                            adapter.addAlarm(alarmToRemove, removedAlarmPosition)
                            AlarmStorage.addItem(alarmToRemove)
                            AlarmManagerHelper.setAlarm(alarmToRemove)
                        }.show()
                    }
                }
            })
    }
}
