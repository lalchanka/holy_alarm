package com.dmytrokoniev.holyalarm.bus

import kotlinx.coroutines.channels.ConflatedBroadcastChannel

object EventBus {

    private var eventsPipeline: ConflatedBroadcastChannel<UiEvent>? = null

    fun initialize() {
        eventsPipeline = ConflatedBroadcastChannel()
    }

    /**
     * Sends [UiEvent]s only while the [EventBus] is alive. This means that you should use
     * the [onReceiveEvent] only After the [initialize] and Before the [dispose] function calls.
     */
    suspend fun onSendEvent(event: UiEvent) {
        eventsPipeline?.send(event)
    }

    /**
     * Receives [UiEvent]s only while the [EventBus] is alive. This means that you should use
     * the [onReceiveEvent] only After the [initialize] and Before the [dispose] function calls.
     * Otherwise [IllegalStateException] will be thrown.
     */
    suspend fun onReceiveEvent(): UiEvent = requireReceive()

    fun dispose() {
        eventsPipeline?.close()
        eventsPipeline = null
    }

    private suspend fun requireReceive(): UiEvent =
        eventsPipeline?.openSubscription()?.receive()
        ?: throw IllegalStateException("Calling onReceiveEvent outside of EventBus lifecycle")
}
