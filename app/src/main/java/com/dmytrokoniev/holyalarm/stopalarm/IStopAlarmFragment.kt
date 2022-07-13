package com.dmytrokoniev.holyalarm.stopalarm

import com.dmytrokoniev.holyalarm.data.AlarmItem

interface IStopAlarmFragment {

    fun onShowSuccess(alarmId: AlarmItem?)

    fun onShowError()
}
