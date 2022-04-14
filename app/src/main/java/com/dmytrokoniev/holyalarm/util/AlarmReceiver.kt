package com.dmytrokoniev.holyalarm.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.dmytrokoniev.holyalarm.ui.MainActivity
import java.util.logging.Logger

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val intentToStart = Intent(context, MainActivity::class.java)
        context?.startActivity(intentToStart)
    }
}
