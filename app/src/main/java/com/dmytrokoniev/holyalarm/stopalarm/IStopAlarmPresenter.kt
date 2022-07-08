package com.dmytrokoniev.holyalarm.stopalarm

import android.content.Context
import com.dmytrokoniev.holyalarm.data.AlarmItem
import kotlinx.coroutines.CoroutineScope

interface IStopAlarmPresenter {

    fun initialize(context: Context, coroutineScope: CoroutineScope?, alarmId: String)

    fun playRingtone()

    fun stopRingtone()

    fun getAlarmItem() : AlarmItem?

    fun onStopAlarmClick()

    fun dispose()
}
