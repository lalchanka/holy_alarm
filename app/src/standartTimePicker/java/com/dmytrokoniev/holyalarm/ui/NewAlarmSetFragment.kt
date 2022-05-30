package com.dmytrokoniev.holyalarm.ui

import com.dmytrokoniev.holyalarm.data.storage.Storage

class NewAlarmSetFragment : AlarmSetFragment() {

    override val alarmIdProvider: () -> String
        get() = {
            (Storage.getLastId() + 1).toString()
        }
}
