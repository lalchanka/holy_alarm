package com.dmytrokoniev.holyalarm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.storage.IAlarmStorage
import com.dmytrokoniev.holyalarm.storage.InMemoryAlarmStorage
import com.dmytrokoniev.holyalarm.storage.SharedPreferencesAlarmStorage
import com.dmytrokoniev.holyalarm.storage.addItems
import com.dmytrokoniev.holyalarm.util.AlarmReceiver
import com.dmytrokoniev.holyalarm.util.AlarmReceiver.Companion.ACTION_TRIGGER_ALARM

// 05.01.2022 dmytrokoniev@gmail.com TODO: <text of todo>

class MainActivity : AppCompatActivity() {

    val alarmStorage: IAlarmStorage = InMemoryAlarmStorage()
    var alarmStorageSP: IAlarmStorage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmStorageSP = SharedPreferencesAlarmStorage(this)
        alarmStorage.addItems(requireAlarmStorageSP().getItems())

        val alarmListFragment = AlarmListFragment()

        val alarmTriggerAction = intent?.action == ACTION_TRIGGER_ALARM

        if (!alarmTriggerAction) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container_view, alarmListFragment)
            loadFragment(alarmListFragment)
        } else {
            val stopAlarmFragment = StopAlarmFragment()
            val arguments = Bundle()
            arguments.putString(ACTION_TRIGGER_ALARM, "")
            stopAlarmFragment.arguments =
            loadFragment(stopAlarmFragment)
        }
    }

    override fun onStop() {
        super.onStop()
        requireAlarmStorageSP().addItems(alarmStorage.getItems())
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmStorageSP = null
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_view, fragment)
            .disallowAddToBackStack()
            .commit()
    }

    fun onAddAlarmClick() = loadFragment(AlarmSetFragment())

    fun onCancelClick() = loadFragment(AlarmListFragment())

    fun onConfirmClick(newAlarm: AlarmItem) {
        alarmStorage.addItem(newAlarm)
        loadFragment(AlarmListFragment())
    }

    private fun requireAlarmStorageSP(): IAlarmStorage =
        alarmStorageSP ?: throw Exception("Out of LifeCycle.")
}
