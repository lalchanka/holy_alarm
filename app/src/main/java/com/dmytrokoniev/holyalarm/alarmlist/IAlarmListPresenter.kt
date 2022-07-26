package com.dmytrokoniev.holyalarm.alarmlist

import com.dmytrokoniev.holyalarm.IBasePresenter
import com.dmytrokoniev.holyalarm.data.AlarmItem

interface IAlarmListPresenter : IBasePresenter {

    fun onAddOneMinuteAlarmClicked()

    fun onAddAlarmClicked()

    fun addAlarm(alarmItem: AlarmItem)

    fun removeAlarm(alarmItem: AlarmItem)

    fun getAlarmList() : List<AlarmItem>
}
