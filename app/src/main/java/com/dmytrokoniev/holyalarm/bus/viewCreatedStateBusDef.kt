package com.dmytrokoniev.holyalarm.bus

import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object ViewCreatedStateBus {

    private lateinit var _viewCreatedStateFlow: MutableStateFlow<ViewCreatedState>
    val viewCreatedStateFlow: StateFlow<ViewCreatedState>
        get() = _viewCreatedStateFlow.asStateFlow()
    lateinit var initialState: ViewCreatedState

    fun initialize() {
        _viewCreatedStateFlow = MutableStateFlow(initialState)
    }

    /**
     * Sends [ViewCreatedState]s only while the [ViewCreatedStateBus] is alive. This means that you should use
     * the [emitViewCreatedState] only After the [initialize] and Before the [dispose] function calls.
     */
    fun emitViewCreatedState(viewCreatedState: ViewCreatedState) {
        _viewCreatedStateFlow.update { viewCreatedState }
    }
}

sealed interface ViewCreatedState {
    
    data class OnSuccess(val view: ViewType): ViewCreatedState
    
    data class OnError(val view: ViewType): ViewCreatedState
}

enum class ViewType {
    TEMP,
    STOP_ALARM
}
