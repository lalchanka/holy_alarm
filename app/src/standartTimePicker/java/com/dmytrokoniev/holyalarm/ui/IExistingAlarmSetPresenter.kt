package com.dmytrokoniev.holyalarm.ui

import kotlinx.coroutines.CoroutineScope

interface IExistingAlarmSetPresenter : IAlarmSetPresenter {

    override fun initialize(scope: CoroutineScope)

    override fun onTimeChanged(hour: Int, minute: Int)

    override fun dispose()
}
