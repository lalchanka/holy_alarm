package com.dmytrokoniev.holyalarm.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import java.util.logging.Logger

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Received broadcast", Toast.LENGTH_SHORT).show()
        Log.d("AlarmReceiver", "Received broadcast")
    }
}
