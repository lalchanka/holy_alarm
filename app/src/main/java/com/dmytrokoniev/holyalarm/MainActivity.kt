package com.dmytrokoniev.holyalarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.alarmlist.AlarmListFragment
import com.dmytrokoniev.holyalarm.bus.AlarmItemBus
import com.dmytrokoniev.holyalarm.bus.AlarmItemViewHolderEvent
import com.dmytrokoniev.holyalarm.bus.AlarmListFragmentEvent
import com.dmytrokoniev.holyalarm.bus.AppState
import com.dmytrokoniev.holyalarm.bus.AppStateBus
import com.dmytrokoniev.holyalarm.bus.EventBus
import com.dmytrokoniev.holyalarm.bus.StopAlarmFragmentEvent
import com.dmytrokoniev.holyalarm.bus.ToolbarEvent
import com.dmytrokoniev.holyalarm.bus.alarmItem
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.data.storage.Storage
import com.dmytrokoniev.holyalarm.data.storage.updateItemIsEnabled
import com.dmytrokoniev.holyalarm.stopalarm.StopAlarmFragment
import com.dmytrokoniev.holyalarm.ui.AlarmSetFragment.Companion.KEY_ALARM_ID
import com.dmytrokoniev.holyalarm.ui.ExistingAlarmSetFragment
import com.dmytrokoniev.holyalarm.ui.NewAlarmSetFragment
import com.dmytrokoniev.holyalarm.util.AlarmManagerHelper
import com.dmytrokoniev.holyalarm.util.ToolbarState
import com.dmytrokoniev.holyalarm.util.ToolbarStateManager
import com.dmytrokoniev.holyalarm.util.launchInActivityScope
import com.dmytrokoniev.holyalarm.util.setAlarm
import com.dmytrokoniev.holyalarm.util.toast

// TODO: d.koniev 03.05.2022 alarm at same time functionality
class MainActivity : AppCompatActivity() {

    private var toolbar: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        initSingletons()
        showInitialFragment()
        initClickListeners()
        startListeningUiEvents()
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
        disposeSingletons()
    }

    private fun initSingletons() {
        AlarmManagerHelper.initialize(this)
        Storage.initialize(this)
    }

    private fun disposeSingletons() {
        AlarmManagerHelper.dispose()
        Storage.dispose()
    }

    private fun showInitialFragment() {
        val alarmTriggeredId = intent?.getStringExtra(KEY_ALARM_ID)
        val isAlarmTriggered = alarmTriggeredId != null
        if (!isAlarmTriggered) {
            ToolbarStateManager.onStateChanged(toolbar, ToolbarState.ICON_CLEAN)
            loadFragment(AlarmListFragment())
        } else {
            showStopAlarmFragment(alarmTriggeredId)
        }
    }

    private fun initClickListeners() {
        val btnCancel = findViewById<View>(R.id.btn_cancel)
        val btnConfirm = findViewById<View>(R.id.btn_confirm)

        btnConfirm.setOnClickListener {
            launchInActivityScope {
                EventBus.emitEvent(ToolbarEvent.ConfirmClicked)
            }
        }
        btnCancel.setOnClickListener {
            launchInActivityScope {
                EventBus.emitEvent(ToolbarEvent.CancelClicked)
            }
        }
    }

    private fun startListeningUiEvents() {
        launchInActivityScope {
            EventBus.eventsFlow.collect {
                when (it) {
                    is AlarmListFragmentEvent.AddClicked -> onAddAlarmClick()
                    is AlarmItemViewHolderEvent.AlarmSet -> onAlarmSet()
                    is AlarmItemViewHolderEvent.AlarmOn -> {
                        val alarmItem = AlarmItemBus.alarmItem
                        onCheckedChangeListener(isChecked = true, alarmItem)
                    }
                    is AlarmItemViewHolderEvent.AlarmOff -> {
                        val alarmItem = AlarmItemBus.alarmItem
                        onCheckedChangeListener(isChecked = false, alarmItem)
                    }
                    is StopAlarmFragmentEvent.StopClicked -> {
                        val alarmItem = AlarmItemBus.alarmItem
                        onStopClick(alarmItem.id)
                    }
                    is ToolbarEvent.ConfirmClicked -> {
                        val appState = AppStateBus.appStateFlow.value
                        val alarmItem = AlarmItemBus.alarmItem
                        if (appState.isAlarmUpdateFlow) {
                            confirmSetAlarm(alarmItem)
                        } else {
                            confirmAddAlarm(alarmItem)
                        }
                    }
                    is ToolbarEvent.CancelClicked -> {
                        ToolbarStateManager.onStateChanged(toolbar, ToolbarState.ICON_CLEAN)
                        loadFragment(AlarmListFragment())
                    }
                }
            }
        }
    }

    private fun onAddAlarmClick() {
        AppStateBus.emitAppState(AppState(isAlarmUpdateFlow = false))
        ToolbarStateManager.onStateChanged(toolbar, ToolbarState.CONFIRM_CANCEL)
        loadFragment(NewAlarmSetFragment())
    }

    private fun onAlarmSet() {
        AppStateBus.emitAppState(AppState(isAlarmUpdateFlow = true))
        ToolbarStateManager.onStateChanged(toolbar, ToolbarState.CONFIRM_CANCEL)
        loadFragment(ExistingAlarmSetFragment())
    }

    private fun onCheckedChangeListener(isChecked: Boolean, alarmItem: AlarmItem) {
        val alarmId = alarmItem.id
        if (isChecked) {
            AlarmManagerHelper.setAlarm(alarmItem)
            Storage.updateItemIsEnabled(alarmId, isEnabled = true)
            toast("Alarm set for: ${alarmItem.hour}:${alarmItem.minute}")
        } else {
            AlarmManagerHelper.cancelAlarm(alarmId)
            Storage.updateItemIsEnabled(alarmId, isEnabled = false)
            toast("Cancelled alarm: ${alarmItem.hour}:${alarmItem.minute}")
        }
    }

    private fun onStopClick(alarmId: String) {
        AlarmManagerHelper.cancelAlarm(alarmId)
        Storage.updateItemIsEnabled(alarmId, isEnabled = false)
        ToolbarStateManager.onStateChanged(toolbar, ToolbarState.ICON_CLEAN)
        loadFragment(AlarmListFragment())
    }

    private fun showStopAlarmFragment(alarmTriggeredId: String?) {
        val stopAlarmFragment = StopAlarmFragment()
        val arguments = Bundle()
        arguments.putString(KEY_ALARM_ID, alarmTriggeredId)
        stopAlarmFragment.arguments = arguments
        ToolbarStateManager.onStateChanged(toolbar, ToolbarState.ICON_CLEAN)
        loadFragment(stopAlarmFragment)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_view, fragment)
            .commit()
    }

    private fun setAlarm(alarmItem: AlarmItem) {
        AlarmManagerHelper.setAlarm(alarmItem)
        ToolbarStateManager.onStateChanged(toolbar, ToolbarState.ICON_CLEAN)
        loadFragment(AlarmListFragment())
    }

    private fun confirmAddAlarm(alarmItem: AlarmItem) {
        Storage.addItem(alarmItem)
        setAlarm(alarmItem)
    }

    private fun confirmSetAlarm(alarmItem: AlarmItem) {
        Storage.updateItem(alarmItem)
        setAlarm(alarmItem)
        toast("Alarm updated: ${alarmItem.hour}:${alarmItem.minute}")
    }

    companion object {
        fun ToolbarStateManager.onStateChanged(toolbar: View?, state: ToolbarState) {
            if (toolbar == null) return
            onStateChanged(toolbar, state)
        }
    }
}
