package com.dmytrokoniev.holyalarm.ui

import com.dmytrokoniev.holyalarm.bus.AlarmItemBus
import com.dmytrokoniev.holyalarm.bus.alarmItem
import com.dmytrokoniev.holyalarm.data.AlarmItem
import kotlinx.coroutines.launch

class ExistingAlarmSetPresenter(
    private val view: IExistingAlarmSetFragment
) : IExistingAlarmSetPresenter, BaseAlarmSetPresenter() {

    private lateinit var alarmItem: AlarmItem
    override val alarmIdProvider: () -> String
        get() = { alarmItem.id }

    override fun getExistingAlarmItem(): AlarmItem {
        scope?.launch { alarmItem = AlarmItemBus.alarmItem }
        return alarmItem
    }
}
