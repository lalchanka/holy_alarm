package com.dmytrokoniev.holyalarm.stopalarm

import android.content.Context

interface IStopAlarmPresenter {

    fun initialize(context: Context)

    fun playRingtone()

    fun dispose()
}
