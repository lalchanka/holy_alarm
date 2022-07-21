package com.dmytrokoniev.holyalarm.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.dmytrokoniev.holyalarm.data.storage.LastIdStorage
import com.dmytrokoniev.holyalarm.data.storage.getLastId

class NewAlarmSetFragment : INewAlarmSetFragment, AlarmSetFragment() {

    private val alarmSetPresenter: INewAlarmSetPresenter = NewAlarmSetPresenter(this)

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
