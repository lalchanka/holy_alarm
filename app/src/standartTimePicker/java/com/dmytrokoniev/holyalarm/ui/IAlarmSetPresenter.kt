package com.dmytrokoniev.holyalarm.ui

import com.dmytrokoniev.holyalarm.IBasePresenter

interface IAlarmSetPresenter : IBasePresenter {

    fun onTimeChanged(hour: Int, minute: Int)
}
