package com.dmytrokoniev.holyalarm.stopalarm

import android.content.Context
import com.dmytrokoniev.holyalarm.data.AlarmItem
import kotlinx.coroutines.CoroutineScope

interface IStopAlarmPresenter {

    fun initialize(context: Context, coroutineScope: CoroutineScope?)

    fun validateData(alarmId: String?)

    fun playRingtone(alarmItem: AlarmItem?)

    fun stopRingtone()

    fun onStopAlarmClick(alarmItem: AlarmItem?)

    fun dispose()
}
