package com.dmytrokoniev.holyalarm.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope

class ExistingAlarmSetFragment : IExistingAlarmSetFragment, AlarmSetFragment() {

    override val presenter: IExistingAlarmSetPresenter = ExistingAlarmSetPresenter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tpAlarmTime?.setOnTimeChangedListener { _, hourOfDay, minute ->
            presenter.onTimeChanged(hourOfDay, minute)
        }
    }
}
