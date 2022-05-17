package com.dmytrokoniev.holyalarm.bus

import com.dmytrokoniev.holyalarm.ui.AlarmItem
import kotlinx.coroutines.channels.Channel

object AlarmItemBus {

    private var alarmItemPipeline: Channel<AlarmItem>? = null

    fun initialize() {
        alarmItemPipeline = Channel()
    }

    /**
     * Sends [AlarmItem]s only while the [AlarmItemBus] is alive. This means that you should use
     * the [onReceiveAlarmItem] only After the [initialize] and Before the [dispose] function calls.
     */
    suspend fun onSendAlarmItem(alarmItem: AlarmItem) {
        alarmItemPipeline?.send(alarmItem)
    }

    /**
     * Receives [AlarmItem]s only while the [AlarmItemBus] is alive. This means that you should use
     * the [onReceiveAlarmItem] only After the [initialize] and Before the [dispose] function calls.
     * Otherwise [IllegalStateException] will be thrown.
     */
    suspend fun onReceiveAlarmItem(): AlarmItem = requireReceive()

    fun dispose() {
        alarmItemPipeline?.close()
        alarmItemPipeline = null
    }

    private suspend fun requireReceive(): AlarmItem =
        alarmItemPipeline?.receive()
        ?: throw IllegalStateException("Calling onReceiveEvent outside of EventBus lifecycle")
}
