package com.dmytrokoniev.holyalarm.util

import com.dmytrokoniev.holyalarm.ui.AlarmItem
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED

@kotlinx.coroutines.ObsoleteCoroutinesApi
object EventBus {

    private var eventsPipeline: BroadcastChannel<UiEvent>? = null

    fun initialize() {
        eventsPipeline = BroadcastChannel(CONFLATED)
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
        eventsPipeline?.cancel()
        eventsPipeline = null
    }

    private suspend fun requireReceive(): UiEvent =
        eventsPipeline?.openSubscription()?.receive()
        ?: throw IllegalStateException("Calling onReceiveEvent outside of EventBus lifecycle")
}

sealed interface UiEvent

sealed interface StopAlarmFragmentEvent : UiEvent {

    data class StopClicked(
        val alarmId: String
    ) : StopAlarmFragmentEvent
}

sealed interface AlarmItemViewHolderEvent : UiEvent {

    // TODO: danylo.oliinyk@gmail.com 17/05/2022 We shouldn't pass non-UI related data in UI events
    data class AlarmOn(
        val alarmItem: AlarmItem
    ) : AlarmItemViewHolderEvent

    data class AlarmOff(
        val alarmItem: AlarmItem
    ) : AlarmItemViewHolderEvent
}

sealed interface AlarmListFragmentEvent : UiEvent {

    object AddClicked : AlarmListFragmentEvent
}
