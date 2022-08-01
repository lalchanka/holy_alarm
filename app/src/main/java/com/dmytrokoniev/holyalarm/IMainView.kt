package com.dmytrokoniev.holyalarm

import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.bus.UiEvent
import com.dmytrokoniev.holyalarm.util.ToolbarState

interface IMainView {

    fun loadFragment(fragment: Fragment)

    fun onUiEventProcessed(event: UiEvent)
}
