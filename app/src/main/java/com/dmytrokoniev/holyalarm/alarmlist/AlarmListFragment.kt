package com.dmytrokoniev.holyalarm.alarmlist

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dmytrokoniev.holyalarm.BuildConfig
import com.dmytrokoniev.holyalarm.R
import com.dmytrokoniev.holyalarm.bus.AlarmListFragmentEvent.AddClicked
import com.dmytrokoniev.holyalarm.bus.EventBus
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.data.SortierStandart
import com.dmytrokoniev.holyalarm.data.storage.AlarmStorage
import com.dmytrokoniev.holyalarm.util.*
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.Disposable
import java.util.*


// TODO add all LC functions with Logs
class AlarmListFragment : Fragment(R.layout.fragment_alarm_list), TextToSpeech.OnInitListener {

    private var adapter: AlarmListAdapter? = null
    private var tts: TextToSpeech? = null
    private var disposable: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvAlarmList = view.findViewById<RecyclerView>(R.id.rv_alarms_list)
        val btnAddAlarm = view.findViewById<Button>(R.id.btn_add_alarm)

        adapter = AlarmListAdapter()
        val alarms = AlarmStorage.getItems()
        val sortedAlarms = SortierStandart.sortAscending(alarms).toList()
        adapter?.setAlarmList(sortedAlarms)
        adapter?.setLaunchInFragmentScope(::launchInFragmentScope)
        rvAlarmList.adapter = adapter

        val textProvider = TextProvider()
        tts = TextToSpeech(context, this)

        btnAddAlarm.setOnClickListener {
            if (BuildConfig.BUILD_TYPE == "debug") {
                onAddOneMinuteAlarmClicked()
            } else if (BuildConfig.BUILD_TYPE == "release") {
//                val singleTextProvier = textProvider.provideText(1, 1)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                disposable = singleTextProvier.subscribe { text ->
//                    tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
//                }
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
                        AlarmStorage.deleteItem(alarmToRemove)
                        AlarmManagerHelper.cancelAlarm(alarmToRemove)

                        Snackbar.make(
                            viewHolder.itemView,
                            "Alarm removed",
                            Snackbar.LENGTH_LONG
                        ).setAction("UNDO") {
                            adapter?.addAlarm(alarmToRemove, removedAlarmPosition)
                            AlarmStorage.addItem(alarmToRemove)
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
        AlarmStorage.addItem(alarmItem)
        AlarmManagerHelper.setAlarm(alarmItem)
        adapter?.addAlarm(alarmItem)
        adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        disposable?.dispose()
        disposable = null
    }

    // TODO: 22/06/2022 Dima Koniev:
    //  TTS function realisation
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language not supported!")
            }
        } else {
            Toast.makeText(this.context, "tts not inited", Toast.LENGTH_SHORT)
        }
    }

    override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}
