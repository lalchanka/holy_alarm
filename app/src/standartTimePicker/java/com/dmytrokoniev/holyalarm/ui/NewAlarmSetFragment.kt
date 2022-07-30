package com.dmytrokoniev.holyalarm.ui

import android.os.Bundle
import android.view.View

class NewAlarmSetFragment : INewAlarmSetFragment, BaseAlarmSetFragment() {

    override val presenter: INewAlarmSetPresenter = NewAlarmSetPresenter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tpAlarmTime?.setOnTimeChangedListener { _, hourOfDay, minute ->
            presenter.onTimeChanged(hourOfDay, minute)
        }
    }
}
