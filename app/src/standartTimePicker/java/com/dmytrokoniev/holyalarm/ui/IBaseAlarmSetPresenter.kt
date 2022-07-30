package com.dmytrokoniev.holyalarm.ui

import com.dmytrokoniev.holyalarm.IBasePresenter

interface IBaseAlarmSetPresenter : IBasePresenter {

    fun onTimeChanged(hour: Int, minute: Int)
}
