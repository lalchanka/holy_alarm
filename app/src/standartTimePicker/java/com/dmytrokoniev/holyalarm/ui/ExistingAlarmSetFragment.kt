package com.dmytrokoniev.holyalarm.ui

import android.os.Bundle
import android.view.View
import com.dmytrokoniev.holyalarm.bus.AlarmItemBus
import com.dmytrokoniev.holyalarm.bus.alarmItem
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.util.launchInFragmentScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExistingAlarmSetFragment : AlarmSetFragment() {

    private lateinit var alarmItem: AlarmItem
    override val alarmIdProvider: () -> String
        get() = { alarmItem.id }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchInFragmentScope {
            alarmItem = AlarmItemBus.alarmItem
            withContext(Dispatchers.Main) {
                tpAlarmTime?.hour = alarmItem.hour
                tpAlarmTime?.minute = alarmItem.minute
            }
        }
    }
}
