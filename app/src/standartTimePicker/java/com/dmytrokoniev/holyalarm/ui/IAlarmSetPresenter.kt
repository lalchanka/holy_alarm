package com.dmytrokoniev.holyalarm.ui

import kotlinx.coroutines.CoroutineScope

interface IAlarmSetPresenter {

    fun initialize(scope: CoroutineScope)

    fun onTimeChanged(hour: Int, minute: Int)

    fun dispose()
}
