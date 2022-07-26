package com.dmytrokoniev.holyalarm

import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.util.ToolbarState

interface IMainView {

    fun showToast(message: String)

    fun changeToolbarState(state: ToolbarState)
    fun loadFragment(fragment: Fragment)
}
