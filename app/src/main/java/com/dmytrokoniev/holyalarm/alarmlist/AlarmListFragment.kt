package com.dmytrokoniev.holyalarm.alarmlist

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.BuildConfig
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.bus.AlarmListFragmentEvent.AddClicked
import com.dmytrokoniev.holyalarm.bus.EventBus
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.data.storage.SpAlarmItemStorage
import com.dmytrokoniev.holyalarm.data.storage.Storage
import com.dmytrokoniev.holyalarm.util.*
import com.google.android.material.snackbar.Snackbar
import java.util.*


// TODO add all LC functions with Logs
class AlarmListFragment : Fragment(R.layout.fragment_alarm_list), TextToSpeech.OnInitListener {

    private var adapter: AlarmListAdapter? = null
    private var tts: TextToSpeech? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvAlarmList = view.findViewById<RecyclerView>(R.id.rv_alarms_list)
        val btnAddAlarm = view.findViewById<Button>(R.id.btn_add_alarm)

        adapter = AlarmListAdapter()
        val alarms = Storage.getItems()
        adapter?.setAlarmList(alarms)
        adapter?.setLaunchInFragmentScope(::launchInFragmentScope)
        rvAlarmList.adapter = adapter

        tts = TextToSpeech(context, this)

        btnAddAlarm.setOnClickListener {
            if (BuildConfig.BUILD_TYPE == "debug") {
                onAddOneMinuteAlarmClicked()
            } else if (BuildConfig.BUILD_TYPE == "release") {
//                    tts!!.speak("darova chuvaachok", TextToSpeech.QUEUE_FLUSH, null,"")
                launchInFragmentScope {
                    EventBus.emitEvent(AddClicked)
                }
            }
        }

        val touchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val removedAlarmPosition = viewHolder.bindingAdapterPosition
                    adapter?.removeAlarm(removedAlarmPosition) { alarmToRemove ->
                        SpAlarmItemStorage.deleteItem(alarmToRemove)
                        AlarmManagerHelper.cancelAlarm(alarmToRemove)

                        Snackbar.make(
                            viewHolder.itemView,
                            "Alarm removed",
                            Snackbar.LENGTH_LONG
                        ).setAction("UNDO") {
                            adapter?.addAlarm(alarmToRemove, removedAlarmPosition)
                            SpAlarmItemStorage.addItem(alarmToRemove)
                            AlarmManagerHelper.setAlarm(alarmToRemove)
                        }.show()
                    }
                }
            })
        touchHelper.attachToRecyclerView(rvAlarmList)
    }

    // TODO: 5/27/2022 Dima Koniev:
    //  Change adapter?.notifyDataSetChanged
    //  with more specific function
    private fun onAddOneMinuteAlarmClicked() {
        val exactTime = Calendar.getInstance()
        val alarmItem = AlarmItem(
            id = Random().nextInt().toString(),
            hour = exactTime.get(Calendar.HOUR_OF_DAY),
            minute = exactTime.get(Calendar.MINUTE) + 1,
            is24HourView = true,
            isEnabled = true
        )
        SpAlarmItemStorage.addItem(alarmItem)
        AlarmManagerHelper.setAlarm(alarmItem)
        adapter?.addAlarm(alarmItem)
        adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language not supported!")
            }
        } else {

        }
    }
}
