package com.dmytrokoniev.holyalarm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.storage.InMemoryAlarmStorage
import com.dmytrokoniev.holyalarm.storage.IAlarmStorage
import com.dmytrokoniev.holyalarm.storage.SharedPreferencesAlarmStorage
import com.dmytrokoniev.holyalarm.storage.addItem

// 05.01.2022 dmytrokoniev@gmail.com TODO: <text of todo>

class MainActivity : AppCompatActivity(){

    val alarmStorage: IAlarmStorage = InMemoryAlarmStorage()
    private val alarmStorageSP: IAlarmStorage
        get() {
           return SharedPreferencesAlarmStorage(this.baseContext)
        }
    //val alarmStorageSP: IAlarmStorage = SharedPreferencesAlarmStorage(this.baseContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmStorage.addItems(alarmStorageSP.getItems())

        val alarmListFragment = AlarmListFragment()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.container_view, alarmListFragment)
        loadFragment(alarmListFragment)
    }

    override fun onStop() {
        super.onStop()

        alarmStorageSP.addItems(alarmStorage.getItems())
    }

    private fun loadFragment(fragment: Fragment) = supportFragmentManager
        .beginTransaction()
        .replace(R.id.container_view, fragment)
        .disallowAddToBackStack()
        .commit()

    fun onAddAlarmClick() = loadFragment(AlarmSetFragment())

    fun onCancelClick() = loadFragment(AlarmListFragment())

    fun onConfirmClick(newAlarm: AlarmItem) {
        val args = Bundle()
        val alarmListFragment = AlarmListFragment()

        alarmStorage.addItem(newAlarm)
        args.putSerializable("newAlarmItem", newAlarm)
        alarmListFragment.arguments = args

        loadFragment(alarmListFragment)
    }
}
