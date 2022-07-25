package com.dmytrokoniev.holyalarm.stopalarm

import android.content.Context
import com.dmytrokoniev.holyalarm.IBasePresenter
import com.dmytrokoniev.holyalarm.data.AlarmItem
import kotlinx.coroutines.CoroutineScope

interface IStopAlarmPresenter : IBasePresenter {

    fun validateData(alarmId: String?)

    fun playRingtone(alarmItem: AlarmItem?)

    fun stopRingtone()

    fun onStopAlarmClick(alarmItem: AlarmItem?)
}
