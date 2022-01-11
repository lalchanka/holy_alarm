package com.dmytrokoniev.holyalarm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.R

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alarmListFragment = AlarmListFragment()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.container_view, alarmListFragment)
        loadFragment(alarmListFragment)
    }

    private fun loadFragment(fragment: Fragment) = supportFragmentManager
        .beginTransaction()
        .replace(R.id.container_view, fragment)
        .disallowAddToBackStack()
        .commit()

    fun onAddAlarmClick() = loadFragment(AlarmSetFragment())

    fun onCancelClick() = loadFragment(AlarmListFragment())

    fun onConfirmClick(newAlarm: AlarmItem) {
        //
        val args = Bundle()
        val alarmListFragment = AlarmListFragment()

        args.putSerializable("newAlarmItem", newAlarm)
        alarmListFragment.arguments = args

        loadFragment(alarmListFragment)
    }
}
