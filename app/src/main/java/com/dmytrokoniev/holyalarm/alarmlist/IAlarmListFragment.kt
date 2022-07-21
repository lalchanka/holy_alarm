package com.dmytrokoniev.holyalarm.alarmlist

import androidx.recyclerview.widget.ItemTouchHelper

interface IAlarmListFragment {

    fun setAlarmListAdapter(adapter: AlarmListAdapter)

    fun attachTouchHelper(touchHelper: ItemTouchHelper)
}
