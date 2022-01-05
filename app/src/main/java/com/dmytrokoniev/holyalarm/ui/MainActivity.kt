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

        loadFragment(alarmListFragment)

//        supportFragmentManager
//            .beginTransaction()
//            .add(R.id.container_view, fragment)
//            .commit()

    }

    private fun loadFragment(fragment: Fragment) = supportFragmentManager
        .beginTransaction()
        .replace(R.id.container_view, fragment)
        .disallowAddToBackStack()
        .commit()

    fun onAddAlarmClick() {
        val alarmSetFragment = AlarmSetFragment()

        loadFragment(alarmSetFragment)
    }

    fun loadAlarmListFragment() {
        val alarmListFragment = AlarmListFragment()

        loadFragment(alarmListFragment)
    }
}
