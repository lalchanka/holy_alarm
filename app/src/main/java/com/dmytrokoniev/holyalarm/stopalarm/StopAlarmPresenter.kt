package com.dmytrokoniev.holyalarm.stopalarm

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri

class StopAlarmPresenter : IStopAlarmPresenter {

    private lateinit var mediaPlayer: MediaPlayer

    override fun initialize(context: Context) {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        mediaPlayer = MediaPlayer.create(, notification)
    }

    override fun playRingtone() {
        TODO("Not yet implemented")
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }
}
