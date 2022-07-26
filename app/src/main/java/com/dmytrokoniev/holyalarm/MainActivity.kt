package com.dmytrokoniev.holyalarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dmytrokoniev.holyalarm.alarmlist.AlarmListFragment
import com.dmytrokoniev.holyalarm.bus.*
import com.dmytrokoniev.holyalarm.data.AlarmItem
import com.dmytrokoniev.holyalarm.data.storage.*
import com.dmytrokoniev.holyalarm.stopalarm.StopAlarmFragment
import com.dmytrokoniev.holyalarm.ui.ExistingAlarmSetFragment
import com.dmytrokoniev.holyalarm.ui.NewAlarmSetFragment
import com.dmytrokoniev.holyalarm.util.*
import com.dmytrokoniev.holyalarm.util.AlarmManagerHelper.KEY_ALARM_ID

// TODO: d.koniev 03.05.2022 alarm at same time functionality
class MainActivity : AppCompatActivity(), IMainView {

    private var presenter: IMainPresenter? = null
    private var toolbar: View? = null

    // to Presenter
    private var spAlarmStorage: SpAlarmItemStorage? = null
    private var spLastAlarmIdStorage: SpLastAlarmIdStorage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        presenter = MainPresenter(this, this.baseContext)

        // MOVED TO MainPresenter
        initSingletons()
        ViewCreatedStateBus.initialState = ViewCreatedState.OnSuccess(ViewType.TEMP)

        showInitialFragment()
        initClickListeners()

        // to Presenter
        startListeningUiEvents()
        startListeningViewCreatedState()
    }

    // LEAVE IT HERE
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val alarmTriggeredId = intent?.getStringExtra(KEY_ALARM_ID)
        val isAlarmTriggered = alarmTriggeredId != null
        if (isAlarmTriggered) {
            showStopAlarmFragment(alarmTriggeredId)
        }
    }

    // LEAVE IT HERE
    override fun onDestroy() {
        super.onDestroy()
        toolbar = null

        //MOVED TO Main
        disposeSingletons()
    }

    // MOVED TO MainPresenter
    private fun initSingletons() {
        AlarmManagerHelper.initialize(this)
        AlarmStorage.initialize(this)
        LastIdStorage.initialize(this)
        ViewCreatedStateBus.initialize()
        spAlarmStorage = AlarmStorage
        spLastAlarmIdStorage = LastIdStorage
    }

    // MOVED TO MainPresenter
    private fun disposeSingletons() {
        AlarmManagerHelper.dispose()
        AlarmStorage.dispose()
        LastIdStorage.dispose()
        spAlarmStorage?.dispose()
        spLastAlarmIdStorage?.dispose()
    }

    // LEVE IT HERE
    private fun showInitialFragment() {
        val alarmTriggeredId = intent?.getStringExtra(KEY_ALARM_ID)
        val isAlarmTriggered = alarmTriggeredId != null
        if (!isAlarmTriggered) {
            changeToolbarState(ToolbarState.ICON_CLEAN)
            loadFragment(AlarmListFragment())
        } else {
            showStopAlarmFragment(alarmTriggeredId)
        }
    }

    // LEAVE IT HERE
    private fun showStopAlarmFragment(alarmTriggeredId: String?) {
        val stopAlarmFragment = StopAlarmFragment()
        val arguments = Bundle()
        arguments.putString(KEY_ALARM_ID, alarmTriggeredId)
        stopAlarmFragment.arguments = arguments
        changeToolbarState(ToolbarState.CONFIRM_CANCEL)
        loadFragment(stopAlarmFragment)
    }

    // LEAVE IT HERE
    private fun initClickListeners() {
        val btnCancel = findViewById<View>(R.id.btn_cancel)
        val btnConfirm = findViewById<View>(R.id.btn_confirm)
        btnConfirm.setOnClickListener {
            presenter?.onConfirmToolbarClick()
        }
        btnCancel.setOnClickListener {
            presenter?.onCancelToolbarClick()
        }
    }

    // MOVED TO MainPresenter
    private fun startListeningUiEvents() {
        launchInActivityScope {
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
                        loadFragment(AlarmListFragment())
                    }
                }
            }
        }
    }

    // MOVED TO MainPresenter
    private fun startListeningViewCreatedState() {
        launchInActivityScope {
            ViewCreatedStateBus.viewCreatedStateFlow.collect {
                when (it) {
                    is ViewCreatedState.OnSuccess -> handleSuccess(it.view)
                    is ViewCreatedState.OnError -> handleError(it.view)
                }
            }
        }
    }

    // MOVED TO MainPresenter
    private fun handleSuccess(view: ViewType) {
        when (view) {
            ViewType.TEMP -> {
            }
            ViewType.STOP_ALARM -> {
            }
        }
    }

    // MOVED TO MainPresenter
    private fun handleError(view: ViewType) {
        when (view) {
            ViewType.TEMP -> {
            }
            ViewType.STOP_ALARM -> {
            }
        }
    }

    // MOVED TO MainPresenter
    private fun onAddAlarmClick() {
        AppStateBus.emitAppState(AppState(isAlarmUpdateFlow = false))
        ToolbarStateManager.onStateChanged(toolbar, ToolbarState.CONFIRM_CANCEL)
        loadFragment(NewAlarmSetFragment())
    }

    // MOVED TO MainPresenter
    private fun onAlarmSet() {
        AppStateBus.emitAppState(AppState(isAlarmUpdateFlow = true))
        ToolbarStateManager.onStateChanged(toolbar, ToolbarState.CONFIRM_CANCEL)
        loadFragment(ExistingAlarmSetFragment())
    }

    // MOVED TO MainPresenter
    private fun onCheckedChangeListener(isChecked: Boolean, alarmItem: AlarmItem) {
        val alarmId = alarmItem.id

        if (isChecked) {
            AlarmManagerHelper.setAlarm(alarmItem)
            spAlarmStorage?.updateItemIsEnabled(alarmId, isEnabled = true)
            toast("Alarm set for: ${alarmItem.hour}:${alarmItem.minute}")
        } else {
            AlarmManagerHelper.cancelAlarm(alarmId)
            spAlarmStorage?.updateItemIsEnabled(alarmId, isEnabled = false)
            toast("Cancelled alarm: ${alarmItem.hour}:${alarmItem.minute}")
        }
    }

    // LEAVE IT HERE
    override fun showToast(message: String) {
        toast(message)
    }

    // LEAVE IT HERE
    override fun changeToolbarState(state: ToolbarState) {
        ToolbarStateManager.onStateChanged(toolbar, state)
    }

    // LEAVE IT HERE
    override fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_view, fragment)
            .commit()
    }

    // MOVED TO MainPresenter
    private fun onStopClick(alarmId: String) {
        val alarmItem = spAlarmStorage?.getItem(alarmId)
        val alarmIds = spAlarmStorage?.findAlarmIds(
            hour = alarmItem?.hour,
            minute = alarmItem?.minute
        )
        alarmIds?.forEach { id ->
            AlarmManagerHelper.cancelAlarm(id)
            spAlarmStorage?.updateItemIsEnabled(id, isEnabled = false)
        }
        ToolbarStateManager.onStateChanged(toolbar, ToolbarState.ICON_CLEAN)
        loadFragment(AlarmListFragment())
    }

    // MOVED TO MainPresenter
    private fun setAlarm(alarmItem: AlarmItem) {
        AlarmManagerHelper.setAlarm(alarmItem)
        ToolbarStateManager.onStateChanged(toolbar, ToolbarState.ICON_CLEAN)
        loadFragment(AlarmListFragment())
    }

    // MOVED TO MainPresenter
    private fun confirmAddAlarm(alarmItem: AlarmItem) {
        spAlarmStorage?.addItem(alarmItem)
        spLastAlarmIdStorage?.setLastId(alarmItem.id.toInt())
        setAlarm(alarmItem)
    }

    // MOVED TO MainPresenter
    private fun confirmSetAlarm(alarmItem: AlarmItem) {
        spAlarmStorage?.updateItem(alarmItem)
        setAlarm(alarmItem)
        toast("Alarm updated: ${alarmItem.hour}:${alarmItem.minute}")
    }

    companion object {
        fun ToolbarStateManager.onStateChanged(toolbar: View?, state: ToolbarState) {
            if (toolbar == null) return
            onStateChanged(toolbar, state)
        }
    }
}
