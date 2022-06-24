package com.dmytrokoniev.holyalarm.stopalarm

import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.bus.AlarmItemBus
import com.dmytrokoniev.holyalarm.bus.EventBus
import com.dmytrokoniev.holyalarm.bus.StopAlarmFragmentEvent.StopClicked
import com.dmytrokoniev.holyalarm.data.storage.AlarmStorage
import com.dmytrokoniev.holyalarm.data.storage.getItem
import com.dmytrokoniev.holyalarm.ui.AlarmSetFragment.Companion.KEY_ALARM_ID
import com.dmytrokoniev.holyalarm.util.TimeUtils.timeHumanFormat
import com.dmytrokoniev.holyalarm.util.launchInFragmentScope
import kotlinx.coroutines.delay


class StopAlarmFragment : Fragment(R.layout.fragment_stop_alarm) {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnStop = view.findViewById<View>(R.id.btn_stop)
        val tvAlarmTime = view.findViewById<TextView>(R.id.tv_alarm_time)
        val alarmId = arguments?.getString(KEY_ALARM_ID)
        val alarmItem = alarmId?.let { AlarmStorage.getItem(it) }
        val formattedHours = alarmItem?.hour?.timeHumanFormat() ?: "Time"
        val formattedMinutes = alarmItem?.minute?.timeHumanFormat() ?: "Error $ERROR_TRIGGER_TIME"
        tvAlarmTime.text = view.context.getString(
            R.string.alarm_time,
            formattedHours,
            formattedMinutes
        )

        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        mediaPlayer = MediaPlayer.create(context, notification)
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            launchInFragmentScope {
                delay(ALARM_AUTOSTOP_TIME_MS)
                alarmItem?.let {
                    onAlarmStop(it)
                }
            }
        }

        btnStop.setOnClickListener {
            alarmItem?.let {
                launchInFragmentScope {
                    onAlarmStop(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer.release()
    }

    private suspend fun onAlarmStop(alarmItem: AlarmItem) {
        mediaPlayer.stop()
        AlarmItemBus.emitAlarmItem(alarmItem)
        EventBus.emitEvent(StopClicked)
    }

    companion object {
        const val ERROR_TRIGGER_TIME = "(((((:"
        private const val ALARM_AUTOSTOP_TIME_MS = 25_000L
    }
}
