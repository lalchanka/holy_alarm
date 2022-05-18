package com.dmytrokoniev.holyalarm.bus

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

// TODO: danylo.oliinyk@pluto.tv 18.05.2022 update docs of all Bus entities
object EventBus {

    private var _eventsFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventsFlow: SharedFlow<UiEvent>
        get() = _eventsFlow.asSharedFlow()

    /**
     * Sends [UiEvent]s only while the [EventBus] is alive. This means that you should use
     * the [collectEvents] only After the [initialize] and Before the [dispose] function calls.
     */
    suspend fun emitEvent(event: UiEvent) {
        _eventsFlow?.emit(event)
    }

    /**
     * Receives [UiEvent]s only while the [EventBus] is alive. This means that you should use
     * the [collectEvents] only After the [initialize] and Before the [dispose] function calls.
     * Otherwise [IllegalStateException] will be thrown.
     */
    fun events(): SharedFlow<UiEvent> = eventsFlow
}
