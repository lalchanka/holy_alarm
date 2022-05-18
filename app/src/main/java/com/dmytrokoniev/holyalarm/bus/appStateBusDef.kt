package com.dmytrokoniev.holyalarm.bus

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

object AppStateBus {

    private val _appStateFlow: MutableStateFlow<AppState>
    val appStateFlow: StateFlow<AppState>
        get() = _appStateFlow

    init {
        val initialState = AppState(isAlarmUpdateFlow = false)
        _appStateFlow = MutableStateFlow(initialState)
    }

    /**
     * Sends [AppState]s only while the [AppStateBus] is alive. This means that you should use
     * the [emitAppState] only After the [initialize] and Before the [dispose] function calls.
     */
    fun emitAppState(appState: AppState) {
        _appStateFlow.update { appState }
    }
}

data class AppState(
    val isAlarmUpdateFlow: Boolean
)
