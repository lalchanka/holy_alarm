package com.dmytrokoniev.holyalarm.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dmytrokoniev.holyalarm.MainActivity
import com.dmytrokoniev.holyalarm.util.AlarmManagerHelper.KEY_ALARM_ID

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmId = intent?.getStringExtra(KEY_ALARM_ID)
        val intentForActivity = Intent(context, MainActivity::class.java)
        intentForActivity.putExtra(KEY_ALARM_ID, alarmId)
        intentForActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intentForActivity)
    }
}
