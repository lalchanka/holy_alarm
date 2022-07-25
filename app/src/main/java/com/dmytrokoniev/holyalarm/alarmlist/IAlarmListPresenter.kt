package com.dmytrokoniev.holyalarm.alarmlist

import androidx.recyclerview.widget.ItemTouchHelper
import com.dmytrokoniev.holyalarm.data.AlarmItem
import kotlinx.coroutines.CoroutineScope

interface IAlarmListPresenter {

    fun onAddOneMinuteAlarmClicked()

    fun onAddAlarm(alarmItem: AlarmItem)

    fun onRemoveAlarm(alarmItem: AlarmItem)

    fun getAlarmList() : List<AlarmItem>
}
