package com.dmytrokoniev.holyalarm.ui

import com.dmytrokoniev.holyalarm.bus.AlarmItemBus
import com.dmytrokoniev.holyalarm.bus.alarmItem
import com.dmytrokoniev.holyalarm.data.AlarmItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ExistingAlarmSetPresenter(
    private val view: ExistingAlarmSetFragment
) : AlarmSetPresenter() {

    private lateinit var alarmItem: AlarmItem
    override val alarmIdProvider: () -> String
        get() = { alarmItem.id }

    override fun initialize(coroutineScope: CoroutineScope) {
        super.initialize(coroutineScope)

        coroutineScope.launch {
            alarmItem = AlarmItemBus.alarmItem
        }
    }
}
