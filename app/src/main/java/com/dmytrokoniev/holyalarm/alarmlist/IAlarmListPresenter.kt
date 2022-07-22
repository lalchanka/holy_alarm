package com.dmytrokoniev.holyalarm.alarmlist

import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.coroutines.CoroutineScope

interface IAlarmListPresenter {

    fun initialize(coroutineScope: CoroutineScope)

    fun onAddOneMinuteAlarmClicked()

    fun createTouchHelper(): ItemTouchHelper
}
