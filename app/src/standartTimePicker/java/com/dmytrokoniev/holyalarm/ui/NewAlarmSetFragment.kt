package com.dmytrokoniev.holyalarm.ui

import com.dmytrokoniev.holyalarm.data.storage.LastIdStorage
import com.dmytrokoniev.holyalarm.data.storage.getLastId

class NewAlarmSetFragment : AlarmSetFragment() {

    override val alarmIdProvider: () -> String
        get() = {
            (LastIdStorage.getLastId() + 1).toString()
        }
}
