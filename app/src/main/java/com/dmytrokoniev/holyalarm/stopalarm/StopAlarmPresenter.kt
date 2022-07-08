package com.dmytrokoniev.holyalarm.stopalarm

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import com.dmytrokoniev.holyalarm.bus.AlarmItemBus
import com.dmytrokoniev.holyalarm.bus.EventBus
import com.dmytrokoniev.holyalarm.bus.StopAlarmFragmentEvent
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.data.storage.AlarmStorage
import com.dmytrokoniev.holyalarm.data.storage.getItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StopAlarmPresenter() : IStopAlarmPresenter {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var alarmId: String
    private var coroutineScope: CoroutineScope? = null

    override fun initialize(context: Context, coroutineScope: CoroutineScope?, alarmId: String) {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        mediaPlayer = MediaPlayer.create(context, notification)
        this.alarmId = alarmId
        this.coroutineScope = coroutineScope

        playRingtone()
    }

    override fun playRingtone() {
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()

            coroutineScope?.launch {
                delay(ALARM_AUTOSTOP_TIME_MS)
                onStopAlarmClick()
            }
        }
    }

    override fun stopRingtone() = mediaPlayer.stop()

    override fun getAlarmItem() : AlarmItem?  = AlarmStorage.getItem(alarmId)

    override fun onStopAlarmClick() {
        val alarmItem = getAlarmItem()
        coroutineScope?.launch {
            alarmItem?.let {
                AlarmItemBus.emitAlarmItem(alarmItem)
                EventBus.emitEvent(StopAlarmFragmentEvent.StopClicked)
            }
        }

    }

    override fun dispose() {
        mediaPlayer.release()
        coroutineScope = null
    }

    companion object {
        private const val ALARM_AUTOSTOP_TIME_MS = 25_000L
    }
}
