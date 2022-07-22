package com.dmytrokoniev.holyalarm.ui

import kotlinx.coroutines.CoroutineScope

interface IAlarmSetPresenter {

    // TODO: Move initialize and dispose to IBasePresenter
    fun initialize(scope: CoroutineScope)

    fun onTimeChanged(hour: Int, minute: Int)

    fun dispose()
}
