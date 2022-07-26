package com.dmytrokoniev.holyalarm

import android.content.Context
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.MainActivity.Companion.onStateChanged
import com.dmytrokoniev.holyalarm.alarmlist.AlarmListFragment
import com.dmytrokoniev.holyalarm.bus.*
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.data.storage.*
import com.dmytrokoniev.holyalarm.ui.ExistingAlarmSetFragment
import com.dmytrokoniev.holyalarm.ui.NewAlarmSetFragment
import com.dmytrokoniev.holyalarm.util.*
import com.dmytrokoniev.holyalarm.util.AlarmManagerHelper.setAlarm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainPresenter(
    private val view: IMainView,
    private val context: Context
) : IMainPresenter {

    private var coroutineScope: CoroutineScope? = null
    private var spAlarmStorage: SpAlarmItemStorage? = null
    private var spLastAlarmIdStorage: SpLastAlarmIdStorage? = null

    override fun initialize(scope: CoroutineScope) {
        this.coroutineScope = scope
        initSingletones()

        ViewCreatedStateBus.initialState = ViewCreatedState.OnSuccess(ViewType.TEMP)
    }

    override fun initSingletones() {
        AlarmManagerHelper.initialize(context)
        AlarmStorage.initialize(context)
        LastIdStorage.initialize(context)
        ViewCreatedStateBus.initialize()
        spAlarmStorage = AlarmStorage
        spLastAlarmIdStorage = LastIdStorage
    }

    override fun disposeSingletons() {
        AlarmManagerHelper.dispose()
        AlarmStorage.dispose()
        LastIdStorage.dispose()
        spAlarmStorage?.dispose()
        spLastAlarmIdStorage?.dispose()
    }

    override fun startListeningUiEvents() {
        coroutineScope?.launch {
            EventBus.eventsFlow.collect {
                when (it) {
                    is AlarmListFragmentEvent.AddClicked -> onAddAlarmClick()
                    is AlarmItemViewHolderEvent.AlarmSet -> onAlarmSet()
                    is AlarmItemViewHolderEvent.AlarmOn -> {
                        val alarmItem = AlarmItemBus.alarmItem
                        onCheckedChangeListener(isChecked = true, alarmItem)
                    }
                    is AlarmItemViewHolderEvent.AlarmOff -> {
                        val alarmItem = AlarmItemBus.alarmItem
                        onCheckedChangeListener(isChecked = false, alarmItem)
                    }
                    is StopAlarmFragmentEvent.StopClicked -> {
                        val alarmItem = AlarmItemBus.alarmItem
                        onStopClick(alarmItem.id)
                    }
                    is ToolbarEvent.ConfirmClicked -> {
                        val appState = AppStateBus.appStateFlow.value
                        val alarmItem = AlarmItemBus.alarmItem
                        if (appState.isAlarmUpdateFlow) {
                            confirmSetAlarm(alarmItem)
                        } else {
                            confirmAddAlarm(alarmItem)
                        }
                    }
                    is ToolbarEvent.CancelClicked -> {
                        ToolbarStateManager.onStateChanged(toolbar, ToolbarState.ICON_CLEAN)
                        view.loadFragment(AlarmListFragment())
                    }
                }
            }
        }
    }

    override fun onAddAlarmClick() {
        AppStateBus.emitAppState(AppState(isAlarmUpdateFlow = false))
        view.changeToolbarState(ToolbarState.CONFIRM_CANCEL)
        view.loadFragment(NewAlarmSetFragment())
    }

    private fun onAlarmSet() {
        AppStateBus.emitAppState(AppState(isAlarmUpdateFlow = true))
        view.changeToolbarState(ToolbarState.CONFIRM_CANCEL)
        view.loadFragment(ExistingAlarmSetFragment())
    }

    override fun onCheckedChangeListener(isChecked: Boolean, alarmItem: AlarmItem) {
        val alarmId = alarmItem.id

        if (isChecked) {
            AlarmManagerHelper.setAlarm(alarmItem)
            spAlarmStorage?.updateItemIsEnabled(alarmId, isEnabled = true)
            view.showToast("Alarm set for: ${alarmItem.hour}:${alarmItem.minute}")
        } else {
            AlarmManagerHelper.cancelAlarm(alarmId)
            spAlarmStorage?.updateItemIsEnabled(alarmId, isEnabled = false)
            view.showToast("Cancelled alarm: ${alarmItem.hour}:${alarmItem.minute}")
        }
    }

    override fun onStopClick(alarmId: String) {
        val alarmItem = spAlarmStorage?.getItem(alarmId)
        val alarmIds = spAlarmStorage?.findAlarmIds(
            hour = alarmItem?.hour,
            minute = alarmItem?.minute
        )
        alarmIds?.forEach { id ->
            AlarmManagerHelper.cancelAlarm(id)
            spAlarmStorage?.updateItemIsEnabled(id, isEnabled = false)
        }
        view.changeToolbarState(ToolbarState.ICON_CLEAN)
        view.loadFragment(AlarmListFragment())
    }

    override fun onConfirmToolbarClick() {
        coroutineScope?.launch {
            EventBus.emitEvent(ToolbarEvent.ConfirmClicked)
        }
    }

    override fun onCancelToolbarClick() {
        coroutineScope?.launch {
            EventBus.emitEvent(ToolbarEvent.CancelClicked)
        }
    }

    override fun dispose() {
        coroutineScope = null
    }
}
