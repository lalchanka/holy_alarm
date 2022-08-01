package com.dmytrokoniev.holyalarm

import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.bus.ViewType
import com.dmytrokoniev.holyalarm.data.AlarmItem

interface IMainPresenter : IBasePresenter {

    fun initSingletones()

    fun disposeSingletons()

    fun startListeningUiEvents()

    fun onConfirmToolbarClick()

    fun onCancelToolbarClick()

    fun startListeningViewCreatedState()

    fun handleSuccess(view: ViewType)

    fun handleError(view: ViewType)
}
