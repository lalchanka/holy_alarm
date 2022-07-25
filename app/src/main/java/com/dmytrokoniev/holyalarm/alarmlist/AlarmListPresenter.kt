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
import java.util.*

class AlarmListPresenter(
    private val view: IAlarmListFragment
) : IAlarmListPresenter {

    override fun onAddOneMinuteAlarmClicked() {
        val exactTime = Calendar.getInstance()
        val alarmItem = AlarmItem(
            id = Random().nextInt().toString(),
            hour = exactTime.get(Calendar.HOUR_OF_DAY),
            minute = exactTime.get(Calendar.MINUTE) + 1,
            is24HourView = true,
            isEnabled = true
        )
        onAddAlarm(alarmItem)
        view.onAddAlarmToAdapter(alarmItem)
    }

    override fun onAddAlarm(alarmItem: AlarmItem) {
        AlarmStorage.addItem(alarmItem)
        AlarmManagerHelper.setAlarm(alarmItem)
    }

    override fun onRemoveAlarm(alarmItem: AlarmItem) {
        AlarmStorage.deleteItem(alarmItem)
        AlarmManagerHelper.cancelAlarm(alarmItem.id)
    }

    override fun getAlarmList() : List<AlarmItem> {
        val alarms = AlarmStorage.getItems()
        return SortierStandart.sortAscending(alarms).toList()
    }
}
