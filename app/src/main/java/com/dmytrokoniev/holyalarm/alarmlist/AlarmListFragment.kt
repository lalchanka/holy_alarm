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
class AlarmListFragment : Fragment(R.layout.fragment_alarm_list), IAlarmListFragment {

    private val alarmListPresenter: IAlarmListPresenter = AlarmListPresenter(this)
    private var rvAlarmList: RecyclerView? = null
    private var btnAddAlarm: Button? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvAlarmList = view.findViewById(R.id.rv_alarms_list)
        btnAddAlarm = view.findViewById(R.id.btn_add_alarm)
        btnAddAlarm?.setOnClickListener {
            if (BuildConfig.BUILD_TYPE == "debug") {
                alarmListPresenter.onAddOneMinuteAlarmClicked()
            } else if (BuildConfig.BUILD_TYPE == "release") {
                launchInFragmentScope {
                    EventBus.emitEvent(AddClicked)
                }
            }
        }

        adapter.setLaunchInFragmentScope(::launchInFragmentScope)
    }

    override fun setAlarmListAdapter(adapter: AlarmListAdapter) {
        rvAlarmList?.adapter = adapter
    }

    override fun attachTouchHelper(touchHelper: ItemTouchHelper) {
        touchHelper.attachToRecyclerView(rvAlarmList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }
}
