package com.dmytrokoniev.holyalarm.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.storage.SharedPreferencesAlarmStorage
import com.dmytrokoniev.holyalarm.ui.AlarmSetFragment.Companion.KEY_ALARM_ID
import com.dmytrokoniev.holyalarm.util.AlarmHelper
import com.dmytrokoniev.holyalarm.util.AlarmTimeBus
import com.dmytrokoniev.holyalarm.util.ToolbarState
import com.dmytrokoniev.holyalarm.util.ToolbarStateManager
import java.util.*

// TODO: d.koniev 03.05.2022 alarm at same time functionality
class MainActivity : AppCompatActivity() {

    private var toolbar: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCancel = findViewById<View>(R.id.btn_cancel)
        val btnConfirm = findViewById<View>(R.id.btn_confirm)
        toolbar = findViewById<View>(R.id.toolbar)
        AlarmHelper.initialize(this)
        SharedPreferencesAlarmStorage.initialize(this)

        val alarmTriggeredId = intent?.getStringExtra(KEY_ALARM_ID)
        val isAlarmTriggered = alarmTriggeredId != null
        if (!isAlarmTriggered) {
            ToolbarStateManager.onStateChanged(toolbar, ToolbarState.ICON_CLEAN)
            loadFragment(AlarmListFragment())
        } else {
            showStopAlarmFragment(alarmTriggeredId)
        }

        btnConfirm.setOnClickListener {
            AlarmTimeBus.lastAlarmTimeSet?.let {
                val date = Date()
                val calendar = Calendar.getInstance()
                calendar.time = date
                Log.d("AlarmSetFragment", "date CalendarBefore ${calendar.time}")

                calendar.set(Calendar.HOUR_OF_DAY, it.hour)
                calendar.set(Calendar.MINUTE, it.minute)
                calendar.set(Calendar.SECOND, 0)
                val alarmTime = calendar.timeInMillis

                AlarmHelper.setAlarm(alarmTime, it.id)
                SharedPreferencesAlarmStorage.addItem(it)
                ToolbarStateManager.onStateChanged(toolbar, ToolbarState.ICON_CLEAN)
                loadFragment(AlarmListFragment())
            }
        }

        btnCancel.setOnClickListener {
            ToolbarStateManager.onStateChanged(toolbar, ToolbarState.ICON_CLEAN)
            loadFragment(AlarmListFragment())
        }
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
        AlarmHelper.dispose()
        SharedPreferencesAlarmStorage.dispose()
    }

    fun onAddAlarmClick() {
        ToolbarStateManager.onStateChanged(toolbar, ToolbarState.CONFIRM_CANCEL)
        loadFragment(AlarmSetFragment())
    }

    fun onStopClick() {
        // TODO: danylo.oliinyk@pluto.tv 26.04.2022 create mechanism to easily update 1 item by id or smth
        // Turn off switch of a triggered alarm
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

    companion object {
        fun ToolbarStateManager.onStateChanged(toolbar: View?, state: ToolbarState) {
            if (toolbar == null) return
            onStateChanged(toolbar, state)
        }
    }
}
