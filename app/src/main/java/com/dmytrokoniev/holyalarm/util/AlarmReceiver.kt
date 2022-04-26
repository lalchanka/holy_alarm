package com.dmytrokoniev.holyalarm.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.dmytrokoniev.holyalarm.ui.AlarmSetFragment.Companion.TRIGGER_ALARM_TIME_KEY
import com.dmytrokoniev.holyalarm.ui.MainActivity
import java.util.logging.Logger

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val intentToStart = Intent(context, MainActivity::class.java)
        intentToStart.action = ACTION_TRIGGER_ALARM
        val alarmTriggerTime = intent?.getStringExtra(TRIGGER_ALARM_TIME_KEY)
        intentToStart.putExtra(TRIGGER_ALARM_TIME_KEY, alarmTriggerTime)
        intentToStart.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(intentToStart)
    }

    companion object {
        const val ACTION_TRIGGER_ALARM = "ACTION_TRIGGER_ALARM"
    }
}
