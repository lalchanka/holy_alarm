package com.dmytrokoniev.holyalarm.ui

import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.dmytrokoniev.holyalarm.R

abstract class BaseAlarmSetFragment : Fragment(R.layout.standart_fragment_set_alarm),
    IBaseAlarmSetFragment {

    abstract val presenter: IBaseAlarmSetPresenter
    override var tpAlarmTime: TimePicker? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.initialize(lifecycleScope)
        tpAlarmTime = view.findViewById(R.id.tp_alarm_time)
        tpAlarmTime?.setIs24HourView(IS_24_FORMAT_ENABLED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.dispose()
        tpAlarmTime = null
    }

    companion object {
        const val IS_24_FORMAT_ENABLED = true
    }
}
