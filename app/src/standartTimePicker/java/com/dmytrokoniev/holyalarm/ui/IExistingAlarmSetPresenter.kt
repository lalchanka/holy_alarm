package com.dmytrokoniev.holyalarm.ui

import com.dmytrokoniev.holyalarm.data.AlarmItem

interface IExistingAlarmSetPresenter : IBaseAlarmSetPresenter {

    fun getExistingAlarmItem(): AlarmItem
}
