package com.dmytrokoniev.holyalarm.alarmlist

import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.bus.AlarmListFragmentEvent
import com.dmytrokoniev.holyalarm.bus.EventBus
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.data.SortierStandart
import com.dmytrokoniev.holyalarm.data.storage.AlarmStorage
import com.dmytrokoniev.holyalarm.util.AlarmManagerHelper
import com.dmytrokoniev.holyalarm.util.addAlarm
import com.dmytrokoniev.holyalarm.util.setAlarm
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

class AlarmListPresenter(
    private val view: IAlarmListFragment
) : IAlarmListPresenter {

    private var coroutineScope: CoroutineScope? = null

    override fun initialize(coroutineScope: CoroutineScope) {
        this.coroutineScope = coroutineScope
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
        addAlarm(alarmItem)
        view.onAddAlarmToAdapter(alarmItem)
    }

    override fun onAddAlarmClicked() {
        coroutineScope?.launch {
            EventBus.emitEvent(AlarmListFragmentEvent.AddClicked)
        }
    }

    override fun addAlarm(alarmItem: AlarmItem) {
        AlarmStorage.addItem(alarmItem)
        if (alarmItem.isEnabled) AlarmManagerHelper.setAlarm(alarmItem)
    }

    override fun onRemoveAlarm(alarmItem: AlarmItem) {
        AlarmStorage.deleteItem(alarmItem)
        AlarmManagerHelper.cancelAlarm(alarmItem.id)
    }

    override fun getAlarmList() : List<AlarmItem> {
        val alarms = AlarmStorage.getItems()
        return SortierStandart.sortAscending(alarms).toList()
    }

    override fun dispose() {
        coroutineScope = null
    }
}
