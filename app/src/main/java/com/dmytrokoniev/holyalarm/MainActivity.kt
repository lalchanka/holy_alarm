package com.dmytrokoniev.holyalarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.dmytrokoniev.holyalarm.alarmlist.AlarmListFragment
import com.dmytrokoniev.holyalarm.bus.AlarmItemBus
import com.dmytrokoniev.holyalarm.bus.AlarmItemViewHolderEvent
import com.dmytrokoniev.holyalarm.bus.AlarmListFragmentEvent
import com.dmytrokoniev.holyalarm.bus.AppState
import com.dmytrokoniev.holyalarm.bus.AppStateBus
import com.dmytrokoniev.holyalarm.bus.StopAlarmFragmentEvent
import com.dmytrokoniev.holyalarm.bus.ToolbarEvent
import com.dmytrokoniev.holyalarm.bus.UiEvent
import com.dmytrokoniev.holyalarm.bus.alarmItem
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.data.AlarmItem.Companion.timeToHumanFormat
import com.dmytrokoniev.holyalarm.data.storage.findAlarmIds
import com.dmytrokoniev.holyalarm.data.storage.getItem
import com.dmytrokoniev.holyalarm.data.storage.setLastId
import com.dmytrokoniev.holyalarm.data.storage.updateItemIsEnabled
import com.dmytrokoniev.holyalarm.stopalarm.StopAlarmFragment
import com.dmytrokoniev.holyalarm.ui.ExistingAlarmSetFragment
import com.dmytrokoniev.holyalarm.ui.NewAlarmSetFragment
import com.dmytrokoniev.holyalarm.util.AlarmManagerHelper
import com.dmytrokoniev.holyalarm.util.AlarmManagerHelper.KEY_ALARM_ID
import com.dmytrokoniev.holyalarm.util.TimeUtils.timeHumanFormat
import com.dmytrokoniev.holyalarm.util.ToolbarState
import com.dmytrokoniev.holyalarm.util.ToolbarStateManager
import com.dmytrokoniev.holyalarm.util.setAlarm
import com.dmytrokoniev.holyalarm.util.toast

class MainActivity : AppCompatActivity(), IMainView {

    private var presenter: IMainPresenter? = null
    private var toolbar: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        presenter = MainPresenter(this, this.baseContext)
        presenter?.initialize(lifecycleScope)

        showInitialFragment()
        initClickListeners()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val alarmTriggeredId = intent?.getStringExtra(KEY_ALARM_ID)
        val isAlarmTriggered = alarmTriggeredId != null
        if (isAlarmTriggered) {
            showStopAlarmFragment(alarmTriggeredId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        toolbar = null
    }

    override fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_view, fragment)
            .commit()
    }

    override fun onUiEventProcessed(event: UiEvent) {
        when (event) {
            is AlarmListFragmentEvent.AddClicked -> onAddAlarmClick()
            is AlarmItemViewHolderEvent.AlarmSet -> onAlarmItemClick()
            is AlarmItemViewHolderEvent.AlarmOn -> {
                val alarmItem = AlarmItemBus.alarmItem
                onCheckedChangeListener(isChecked = true, alarmItem)
            }
            is AlarmItemViewHolderEvent.AlarmOff -> {
                val alarmItem = AlarmItemBus.alarmItem
                onCheckedChangeListener(isChecked = false, alarmItem)
            }
            is StopAlarmFragmentEvent.StopClicked -> {
                onStopClick()
            }
            is ToolbarEvent.ConfirmClicked -> {
                val appState = AppStateBus.appStateFlow.value
                val alarmItem = AlarmItemBus.alarmItem
                if (appState.isAlarmUpdateFlow) {
                    onSetExistingAlarm(alarmItem)
                } else {
                    onSetNewAlarm()
                }
            }
            is ToolbarEvent.CancelClicked -> {
                onCancelClicked()
            }
        }
    }

    private fun onAddAlarmClick() {
        changeToolbarState(ToolbarState.CONFIRM_CANCEL)
        loadFragment(NewAlarmSetFragment())
    }

    private fun onAlarmItemClick() {
        changeToolbarState(ToolbarState.CONFIRM_CANCEL)
        loadFragment(ExistingAlarmSetFragment())
    }

    private fun onCheckedChangeListener(isChecked: Boolean, alarmItem: AlarmItem) {
        if (isChecked) {
            showToast("Alarm set for: ${alarmItem.timeToHumanFormat()}")
        } else {
            showToast("Cancelled alarm: ${alarmItem.timeToHumanFormat()}")
        }
    }

    private fun onStopClick() {
        changeToolbarState(ToolbarState.ICON_CLEAN)
        loadFragment(AlarmListFragment())
    }

    private fun onSetNewAlarm() {
        changeToolbarState(ToolbarState.ICON_CLEAN)
        loadFragment(AlarmListFragment())
    }

    private fun onSetExistingAlarm(alarmItem: AlarmItem) {
        changeToolbarState(ToolbarState.ICON_CLEAN)
        loadFragment(AlarmListFragment())
        showToast("Alarm updated: ${alarmItem.timeToHumanFormat()}")
    }

    private fun onCancelClicked() {
        changeToolbarState(ToolbarState.ICON_CLEAN)
        loadFragment(AlarmListFragment())
    }

    private fun showToast(message: String) {
        toast(message)
    }

    private fun changeToolbarState(state: ToolbarState) {
        ToolbarStateManager.onStateChanged(toolbar, state)
    }

    private fun showInitialFragment() {
        val alarmTriggeredId = intent?.getStringExtra(KEY_ALARM_ID)
        val isAlarmTriggered = alarmTriggeredId != null
        if (!isAlarmTriggered) {
            changeToolbarState(ToolbarState.ICON_CLEAN)
            loadFragment(AlarmListFragment())
        } else {
            showStopAlarmFragment(alarmTriggeredId)
        }
    }

    private fun showStopAlarmFragment(alarmTriggeredId: String?) {
        val stopAlarmFragment = StopAlarmFragment()
        val arguments = Bundle()
        arguments.putString(KEY_ALARM_ID, alarmTriggeredId)
        stopAlarmFragment.arguments = arguments
        changeToolbarState(ToolbarState.CONFIRM_CANCEL)
        loadFragment(stopAlarmFragment)
    }

    private fun initClickListeners() {
        val btnCancel = findViewById<View>(R.id.btn_cancel)
        val btnConfirm = findViewById<View>(R.id.btn_confirm)
        btnConfirm.setOnClickListener {
            presenter?.onConfirmToolbarClick()
        }
        btnCancel.setOnClickListener {
            presenter?.onCancelToolbarClick()
        }
    }

    companion object {
        fun ToolbarStateManager.onStateChanged(toolbar: View?, state: ToolbarState) {
            if (toolbar == null) return
            onStateChanged(toolbar, state)
        }
    }
}
