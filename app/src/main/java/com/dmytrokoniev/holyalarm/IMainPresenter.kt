package com.dmytrokoniev.holyalarm

import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.data.AlarmItem

interface IMainPresenter : IBasePresenter {

    fun initSingletones()

    fun startListeningUiEvents()

    fun onAddAlarmClick()

    fun onCheckedChangeListener(isChecked: Boolean, alarmItem: AlarmItem)

    fun onStopClick(alarmId: String)

    fun onConfirmToolbarClick()

    fun onCancelToolbarClick()

    fun disposeSingletons()
}
