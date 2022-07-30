package com.dmytrokoniev.holyalarm.ui

import com.dmytrokoniev.holyalarm.bus.AlarmItemBus
import com.dmytrokoniev.holyalarm.data.AlarmItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseAlarmSetPresenter : IBaseAlarmSetPresenter {

    protected var scope: CoroutineScope? = null
    protected abstract val alarmIdProvider: () -> String

    override fun initialize(scope: CoroutineScope) {
        this.scope = scope
    }

    override fun onTimeChanged(hour: Int, minute: Int) {
        val newAlarmId = alarmIdProvider()
        val newAlarm = AlarmItem(
            id = newAlarmId,
            hour = hour,
            minute = minute,
            is24HourView = BaseAlarmSetFragment.IS_24_FORMAT_ENABLED,
            isEnabled = true
        )
        scope?.launch {
            AlarmItemBus.emitAlarmItem(newAlarm)
        }
    }

    override fun dispose() {
        scope = null
    }
}
