package com.dmytrokoniev.holyalarm.alarmlist

import androidx.recyclerview.widget.ItemTouchHelper

interface IAlarmListFragment {

    fun onAdapterInitialized(adapter: AlarmListAdapter)

    fun attachTouchHelper(touchHelper: ItemTouchHelper)
}
