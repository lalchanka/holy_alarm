package com.dmytrokoniev.holyalarm

import android.content.Context
import com.dmytrokoniev.holyalarm.alarmlist.AlarmListFragment
import com.dmytrokoniev.holyalarm.bus.*
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.data.storage.*
import com.dmytrokoniev.holyalarm.ui.ExistingAlarmSetFragment
import com.dmytrokoniev.holyalarm.ui.NewAlarmSetFragment
import com.dmytrokoniev.holyalarm.util.*
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
        ViewCreatedStateBus.initialState = ViewCreatedState.OnSuccess(ViewType.TEMP)
        initSingletones()
        startListeningUiEvents()
        startListeningViewCreatedState()
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
                processUiEvent(it)
                view.onUiEventProcessed(it)
            }
        }
    }

    private fun processUiEvent(event: UiEvent) {
        when (event) {
            is AlarmListFragmentEvent.AddClicked -> onAddAlarmClick()
            is AlarmItemViewHolderEvent.AlarmSet -> onAlarmItemClick()
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
                    onSetNewAlarm(alarmItem)
                } else {
                    onSetExistingAlarm(alarmItem)
                }
            }
            is ToolbarEvent.CancelClicked -> {
                view.changeToolbarState(ToolbarState.ICON_CLEAN)
                view.loadFragment(AlarmListFragment())
            }
        }
    }

    override fun onAddAlarmClick() {
        AppStateBus.emitAppState(AppState(isAlarmUpdateFlow = false))
        view.changeToolbarState(ToolbarState.CONFIRM_CANCEL)
        view.loadFragment(NewAlarmSetFragment())
    }

    override fun onAlarmItemClick() {
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

    override fun setAlarm(alarmItem: AlarmItem) {
        AlarmManagerHelper.setAlarm(alarmItem)
        view.changeToolbarState(ToolbarState.ICON_CLEAN)
        view.loadFragment(AlarmListFragment())
    }

    override fun onSetNewAlarm(alarmItem: AlarmItem) {
        spAlarmStorage?.addItem(alarmItem)
        spLastAlarmIdStorage?.setLastId(alarmItem.id.toInt())
        setAlarm(alarmItem)
    }

    override fun onSetExistingAlarm(alarmItem: AlarmItem) {
        spAlarmStorage?.updateItem(alarmItem)
        setAlarm(alarmItem)
        view.showToast("Alarm updated: ${alarmItem.hour}:${alarmItem.minute}")
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

    override fun startListeningViewCreatedState() {
        coroutineScope?.launch {
            ViewCreatedStateBus.viewCreatedStateFlow.collect {
                when (it) {
                    is ViewCreatedState.OnSuccess -> handleSuccess(it.view)
                    is ViewCreatedState.OnError -> handleError(it.view)
                }
            }
        }
    }

    override fun handleSuccess(view: ViewType) {
        when (view) {
            ViewType.TEMP -> {
            }
            ViewType.STOP_ALARM -> {
            }
        }
    }

    override fun handleError(view: ViewType) {
        when (view) {
            ViewType.TEMP -> {
            }
            ViewType.STOP_ALARM -> {
            }
        }
    }

    override fun dispose() {
        coroutineScope = null
    }
}
