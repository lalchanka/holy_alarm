package com.dmytrokoniev.holyalarm.ui

import com.dmytrokoniev.holyalarm.data.storage.LastIdStorage
import com.dmytrokoniev.holyalarm.data.storage.getLastId

class NewAlarmSetPresenter : INewAlarmSetPresenter, BaseAlarmSetPresenter() {

    override val alarmIdProvider: () -> String
        get() = {
            (LastIdStorage.getLastId() + 1).toString()
        }
}
