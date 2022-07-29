package com.dmytrokoniev.holyalarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.dmytrokoniev.holyalarm.alarmlist.AlarmListFragment
import com.dmytrokoniev.holyalarm.bus.UiEvent
import com.dmytrokoniev.holyalarm.stopalarm.StopAlarmFragment
import com.dmytrokoniev.holyalarm.util.AlarmManagerHelper.KEY_ALARM_ID
import com.dmytrokoniev.holyalarm.util.ToolbarState
import com.dmytrokoniev.holyalarm.util.ToolbarStateManager
import com.dmytrokoniev.holyalarm.util.toast

// TODO: d.koniev 03.05.2022 alarm at same time functionality
class MainActivity : AppCompatActivity(), IMainView {

    private var presenter: IMainPresenter? = null
    private var toolbar: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        presenter = MainPresenter(this, this.baseContext)
        presenter?.initialize(lifecycleScope)

        showInitialFragment()
        initClickListeners()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val alarmTriggeredId = intent?.getStringExtra(KEY_ALARM_ID)
        val isAlarmTriggered = alarmTriggeredId != null
        if (isAlarmTriggered) {
            showStopAlarmFragment(alarmTriggeredId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        toolbar = null
    }

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

    private fun showStopAlarmFragment(alarmTriggeredId: String?) {
        val stopAlarmFragment = StopAlarmFragment()
        val arguments = Bundle()
        arguments.putString(KEY_ALARM_ID, alarmTriggeredId)
        stopAlarmFragment.arguments = arguments
        changeToolbarState(ToolbarState.CONFIRM_CANCEL)
        loadFragment(stopAlarmFragment)
    }

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

    override fun onUiEventProcessed(event: UiEvent) {

    }

    override fun showToast(message: String) {
        toast(message)
    }

    override fun changeToolbarState(state: ToolbarState) {
        ToolbarStateManager.onStateChanged(toolbar, state)
    }

    override fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_view, fragment)
            .commit()
    }

    companion object {
        fun ToolbarStateManager.onStateChanged(toolbar: View?, state: ToolbarState) {
            if (toolbar == null) return
            onStateChanged(toolbar, state)
        }
    }
}
