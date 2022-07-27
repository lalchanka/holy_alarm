package com.dmytrokoniev.holyalarm

import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.bus.ViewType
import com.dmytrokoniev.holyalarm.data.AlarmItem

interface IMainPresenter : IBasePresenter {

    fun initSingletones()

    fun disposeSingletons()

    fun startListeningUiEvents()

    fun onAddAlarmClick()

    fun onAlarmItemClick()

    fun startListeningViewCreatedState()

    fun handleSuccess(view: ViewType)

    fun handleError(view: ViewType)

    fun onCheckedChangeListener(isChecked: Boolean, alarmItem: AlarmItem)

    fun onStopClick(alarmId: String)

    fun onConfirmToolbarClick()

    fun onCancelToolbarClick()

    fun onSetNewAlarm(alarmItem: AlarmItem)

    fun onSetExistingAlarm(alarmItem: AlarmItem)

    fun setAlarm(alarmItem: AlarmItem)
}
