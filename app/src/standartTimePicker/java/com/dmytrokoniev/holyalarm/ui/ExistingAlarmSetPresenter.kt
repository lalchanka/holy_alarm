package com.dmytrokoniev.holyalarm.ui

import com.dmytrokoniev.holyalarm.bus.AlarmItemBus
import com.dmytrokoniev.holyalarm.bus.alarmItem
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.stopalarm.IStopAlarmFragment
import com.dmytrokoniev.holyalarm.util.launchInFragmentScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExistingAlarmSetPresenter(
    private val view: IExistingAlarmSetFragment
) : IExistingAlarmSetPresenter, AlarmSetPresenter() {

    private lateinit var alarmItem: AlarmItem
    override val alarmIdProvider: () -> String
        get() = { alarmItem.id }

    override fun initialize(coroutineScope: CoroutineScope) {
        super.initialize(coroutineScope)

        coroutineScope.launch {
            alarmItem = AlarmItemBus.alarmItem
            withContext(Dispatchers.Main) {
                view.tpAlarmTime?.hour = alarmItem.hour
                view.tpAlarmTime?.minute = alarmItem.minute
            }
        }
    }

    override fun onTimeChanged(hour: Int, minute: Int) {
        super.onTimeChanged(hour, minute)
    }
}
