package com.dmytrokoniev.holyalarm.alarmlist

import com.dmytrokoniev.holyalarm.data.AlarmItem

interface IAlarmListFragment {

    fun onAddAlarmToAdapter(alarmItem: AlarmItem)

    fun createAttachTouchHelper()
}
