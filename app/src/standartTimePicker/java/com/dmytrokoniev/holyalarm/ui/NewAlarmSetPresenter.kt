package com.dmytrokoniev.holyalarm.ui

import com.dmytrokoniev.holyalarm.data.storage.LastIdStorage
import com.dmytrokoniev.holyalarm.data.storage.getLastId
import kotlinx.coroutines.CoroutineScope

class NewAlarmSetPresenter : INewAlarmSetPresenter, AlarmSetPresenter() {

    override val alarmIdProvider: () -> String
        get() = {
            (LastIdStorage.getLastId() + 1).toString()
        }
}
