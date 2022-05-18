package com.dmytrokoniev.holyalarm.bus

import com.dmytrokoniev.holyalarm.ui.AlarmItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AlarmItemBus {

    private val _alarmItemFlow: MutableStateFlow<AlarmItem?> = MutableStateFlow(null)
    val alarmItemFlow: StateFlow<AlarmItem?>
        get() = _alarmItemFlow.asStateFlow()

    /**
     * Sends [AlarmItem]s only while the [AlarmItemBus] is alive. This means that you should use
     * the [emitAlarmItem] only After the [initialize] and Before the [dispose] function calls.
     */
    suspend fun emitAlarmItem(alarmItem: AlarmItem) {
        _alarmItemFlow.emit(alarmItem)
    }
}

val AlarmItemBus.alarmItem: AlarmItem
    get() = alarmItemFlow.value ?: throw IllegalStateException(
        "Please check whether you access the AlarmItem in a right place"
    )
