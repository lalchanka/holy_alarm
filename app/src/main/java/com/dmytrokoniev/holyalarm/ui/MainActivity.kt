package com.dmytrokoniev.holyalarm.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.storage.IAlarmStorage
import com.dmytrokoniev.holyalarm.storage.SharedPreferencesAlarmStorage
import com.dmytrokoniev.holyalarm.ui.AlarmSetFragment.Companion.KEY_ALARM_ID
import com.dmytrokoniev.holyalarm.util.AlarmHelper
import com.dmytrokoniev.holyalarm.util.IToolbar

class MainActivity : AppCompatActivity(), IToolbar {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AlarmHelper.initialize(this)
        SharedPreferencesAlarmStorage.initialize(this)

        val alarmTriggeredId = intent?.getStringExtra(KEY_ALARM_ID)
        val isAlarmTriggered = alarmTriggeredId != null
        if (!isAlarmTriggered) {
            loadFragment(AlarmListFragment())
        } else {
            showStopAlarmFragment(alarmTriggeredId)
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

    private fun showStopAlarmFragment(alarmTriggeredId: String?) {
        val stopAlarmFragment = StopAlarmFragment()
        val arguments = Bundle()
        arguments.putString(KEY_ALARM_ID, alarmTriggeredId)
        stopAlarmFragment.arguments = arguments
        loadFragment(stopAlarmFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        AlarmHelper.dispose()
        SharedPreferencesAlarmStorage.dispose()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_view, fragment)
            .commit()
    }

    fun onAddAlarmClick() = loadFragment(AlarmSetFragment())

    override fun onConfirmClick() {
        TODO("Not yet implemented")
    }

    override fun onCancelClick() = loadFragment(AlarmListFragment())

    fun onConfirmClick(newAlarm: AlarmItem) {
        SharedPreferencesAlarmStorage.addItem(newAlarm)
        loadFragment(AlarmListFragment())
    }

    fun onStopClick() {
        // TODO: danylo.oliinyk@pluto.tv 26.04.2022 create mechanism to easily update 1 item by id or smth
        // Turn off switch of a triggered alarm
        loadFragment(AlarmListFragment())
    }
}
