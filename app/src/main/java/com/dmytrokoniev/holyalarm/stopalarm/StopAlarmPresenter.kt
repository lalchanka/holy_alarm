package com.dmytrokoniev.holyalarm.stopalarm

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import com.dmytrokoniev.holyalarm.bus.*
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.data.storage.AlarmStorage
import com.dmytrokoniev.holyalarm.data.storage.getItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StopAlarmPresenter(
    private val view: IStopAlarmFragment,
    private val context: Context
) : IStopAlarmPresenter {

    private lateinit var mediaPlayer: MediaPlayer
    private var coroutineScope: CoroutineScope? = null

    override fun initialize(coroutineScope: CoroutineScope) {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        mediaPlayer = MediaPlayer.create(context, notification)
        this.coroutineScope = coroutineScope
    }

    override fun playRingtone(alarmItem: AlarmItem?) {
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()

            coroutineScope?.launch {
                delay(ALARM_AUTOSTOP_TIME_MS)
                onStopAlarmClick(alarmItem)
            }
        }
    }

    override fun stopRingtone() = mediaPlayer.stop()

    override fun validateData(alarmId: String?) {
        coroutineScope?.launch {
            if (alarmId != null) {
                ViewCreatedStateBus.emitViewCreatedState(ViewCreatedState.OnSuccess(ViewType.STOP_ALARM))
                val alarmItem = AlarmStorage.getItem(alarmId)
                view.onShowSuccess(alarmItem)
            } else {
                ViewCreatedStateBus.emitViewCreatedState(ViewCreatedState.OnError(ViewType.STOP_ALARM))
                view.onShowError()
            }
        }
    }

    override fun onStopAlarmClick(alarmItem: AlarmItem?) {
        coroutineScope?.launch {
            alarmItem?.let {
                stopRingtone()
                AlarmItemBus.emitAlarmItem(alarmItem)
                EventBus.emitEvent(StopAlarmFragmentEvent.StopClicked)
            }
        }
    }

    override fun dispose() {
        mediaPlayer.release()
    }

    companion object {
        private const val ALARM_AUTOSTOP_TIME_MS = 25_000L
    }
}
