package com.dmytrokoniev.holyalarm.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.bus.AlarmItemBus
import com.dmytrokoniev.holyalarm.bus.AlarmItemViewHolderEvent.*
import com.dmytrokoniev.holyalarm.bus.AlarmListFragmentEvent.AddClicked
import com.dmytrokoniev.holyalarm.bus.EventBus
import com.dmytrokoniev.holyalarm.bus.StopAlarmFragmentEvent.StopClicked
import com.dmytrokoniev.holyalarm.storage.Storage
import com.dmytrokoniev.holyalarm.storage.updateItemIsEnabled
import com.dmytrokoniev.holyalarm.ui.AlarmSetFragment.Companion.KEY_ALARM_ID
import com.dmytrokoniev.holyalarm.util.*
import kotlinx.coroutines.launch

// TODO: d.koniev 03.05.2022 alarm at same time functionality
class MainActivity : AppCompatActivity() {

    private var toolbar: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCancel = findViewById<View>(R.id.btn_cancel)
        val btnConfirm = findViewById<View>(R.id.btn_confirm)
        toolbar = findViewById(R.id.toolbar)
        AlarmManagerHelper.initialize(this)
        Storage.initialize(this)
        EventBus.initialize()
        AlarmItemBus.initialize()

        val alarmTriggeredId = intent?.getStringExtra(KEY_ALARM_ID)
        val isAlarmTriggered = alarmTriggeredId != null
        if (!isAlarmTriggered) {
            ToolbarStateManager.onStateChanged(toolbar, ToolbarState.ICON_CLEAN)
            loadFragment(AlarmListFragment())
        } else {
            showStopAlarmFragment(alarmTriggeredId)
        }

        btnConfirm.setOnClickListener {
            launchInActivityScope {
                val alarmItem = AlarmItemBus.onReceiveAlarmItem()
                AlarmManagerHelper.setAlarm(alarmItem)
                Storage.addItem(alarmItem)
                ToolbarStateManager.onStateChanged(toolbar, ToolbarState.ICON_CLEAN)
                loadFragment(AlarmListFragment())
            }
        }

        btnCancel.setOnClickListener {
            ToolbarStateManager.onStateChanged(toolbar, ToolbarState.ICON_CLEAN)
            loadFragment(AlarmListFragment())
        }
        startListeningForUiEvents()
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
        AlarmManagerHelper.dispose()
        Storage.dispose()
        EventBus.dispose()
    }

    private fun startListeningForUiEvents() {
        lifecycleScope.launch {
            val alarmItem = AlarmItemBus.onReceiveAlarmItem()
            when (EventBus.onReceiveEvent()) {
                is StopClicked -> onStopClick(alarmItem.id)
                is AlarmSet -> onAlarmItemClickListener()
                is AlarmOn -> onCheckedChangeListener(isChecked = true, alarmItem)
                is AlarmOff -> onCheckedChangeListener(isChecked = false, alarmItem)
                is AddClicked -> onAddAlarmClick()
            }
        }
    }

    private fun onStopClick(alarmId: String) {
        AlarmManagerHelper.cancelAlarm(alarmId)
        Storage.updateItemIsEnabled(alarmId, isEnabled = false)
        ToolbarStateManager.onStateChanged(toolbar, ToolbarState.ICON_CLEAN)
        loadFragment(AlarmListFragment())
    }

    private fun onAlarmItemClickListener() {
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

    private fun onAddAlarmClick() {
        ToolbarStateManager.onStateChanged(toolbar, ToolbarState.CONFIRM_CANCEL)
        loadFragment(NewAlarmSetFragment())
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

    companion object {
        fun ToolbarStateManager.onStateChanged(toolbar: View?, state: ToolbarState) {
            if (toolbar == null) return
            onStateChanged(toolbar, state)
        }
    }
}
