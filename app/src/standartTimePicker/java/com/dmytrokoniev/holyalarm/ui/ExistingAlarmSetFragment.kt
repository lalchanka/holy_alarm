package com.dmytrokoniev.holyalarm.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope

class ExistingAlarmSetFragment : IExistingAlarmSetFragment, AlarmSetFragment() {

    private val alarmSetPresenter: IExistingAlarmSetPresenter = ExistingAlarmSetPresenter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alarmSetPresenter.initialize(this.lifecycleScope)

        tpAlarmTime?.setOnTimeChangedListener { _, hourOfDay, minute ->
            alarmSetPresenter.onTimeChanged(hourOfDay, minute)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        alarmSetPresenter.dispose()
    }
}
