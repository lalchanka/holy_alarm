package com.dmytrokoniev.holyalarm.ui

import com.dmytrokoniev.holyalarm.data.storage.LastIdStorage
import com.dmytrokoniev.holyalarm.data.storage.getLastId

class NewAlarmSetFragment : INewAlarmSetFragment, AlarmSetFragment() {

    private val alarmSetPresenter: INewAlarmSetPresenter = NewAlarmSetPresenter(this)


}
