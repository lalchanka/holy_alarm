package com.dmytrokoniev.holyalarm.alarmlist

import kotlinx.coroutines.CoroutineScope

interface IAlarmListPresenter {

    fun initialize(coroutineScope: CoroutineScope)

    fun onAddOneMinuteAlarmClicked()

    fun createTouchHelper()
}
