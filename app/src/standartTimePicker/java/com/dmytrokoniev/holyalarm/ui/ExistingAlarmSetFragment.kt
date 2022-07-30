package com.dmytrokoniev.holyalarm.ui

import android.os.Bundle
import android.view.View

class ExistingAlarmSetFragment : IExistingAlarmSetFragment, BaseAlarmSetFragment() {

    override val presenter: IExistingAlarmSetPresenter = ExistingAlarmSetPresenter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val alarmItem = presenter.getExistingAlarmItem()
        tpAlarmTime?.hour = alarmItem.hour
        tpAlarmTime?.minute = alarmItem.minute
        tpAlarmTime?.setOnTimeChangedListener { _, hourOfDay, minute ->
            presenter.onTimeChanged(hourOfDay, minute)
        }
    }
}
