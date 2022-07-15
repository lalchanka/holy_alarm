package com.dmytrokoniev.holyalarm.ui

import com.dmytrokoniev.holyalarm.bus.AlarmItemBus
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.util.launchInFragmentScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class AlarmSetPresenter : IAlarmSetPresenter {

    private var coroutineScope: CoroutineScope? = null
    protected abstract val alarmIdProvider: () -> String

    override fun initialize(coroutineScope: CoroutineScope) {
        this.coroutineScope = coroutineScope
    }

    override fun onTimeChanged(hour: Int, minute: Int) {
        val newAlarmId = alarmIdProvider()
        val newAlarm = AlarmItem(
            id = newAlarmId,
            hour = hour,
            minute = minute,
            is24HourView = AlarmSetFragment.IS_24_FORMAT_ENABLED,
            isEnabled = true
        )
        coroutineScope?.launch {
            AlarmItemBus.emitAlarmItem(newAlarm)
        }
    }

    override fun dispose() {
        coroutineScope = null
    }
}
