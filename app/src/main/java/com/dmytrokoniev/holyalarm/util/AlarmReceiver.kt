package com.dmytrokoniev.holyalarm.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dmytrokoniev.holyalarm.ui.AlarmItem
import com.dmytrokoniev.holyalarm.ui.AlarmSetFragment.Companion.KEY_ALARM
import com.dmytrokoniev.holyalarm.ui.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val alarm = intent?.getParcelableExtra<AlarmItem>(KEY_ALARM)
        val intentForActivity = Intent(context, MainActivity::class.java)
        intentForActivity.putExtra(KEY_ALARM, alarm)
        intentForActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(intentForActivity)
    }
}
