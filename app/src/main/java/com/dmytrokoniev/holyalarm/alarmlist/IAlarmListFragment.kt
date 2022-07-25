package com.dmytrokoniev.holyalarm.alarmlist

import androidx.recyclerview.widget.ItemTouchHelper
import com.dmytrokoniev.holyalarm.data.AlarmItem

interface IAlarmListFragment {

    fun onAddAlarmToAdapter(alarmItem: AlarmItem)

    fun createTouchHelper(): ItemTouchHelper

    fun attachTouchHelper(touchHelper: ItemTouchHelper)
}
