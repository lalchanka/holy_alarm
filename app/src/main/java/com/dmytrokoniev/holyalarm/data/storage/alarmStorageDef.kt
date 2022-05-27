package com.dmytrokoniev.holyalarm.data.storage

import android.content.Context
import com.dmytrokoniev.holyalarm.data.AlarmItem

interface IAlarmStorage {

    fun initialize(context: Context)

    fun addItem(item: AlarmItem)

    fun getItem(id: String): AlarmItem?

    fun getItems(): List<AlarmItem>

    fun updateItem(item: AlarmItem): Boolean

    fun deleteItem(idToDelete: String): Boolean

    fun clear()

    fun dispose()
}

fun IAlarmStorage.addItems(items: List<AlarmItem>) = items.forEach { addItem(it) }

fun IAlarmStorage.updateItemIsEnabled(id: String, isEnabled: Boolean) {
    val alarmItem = getItem(id) ?: return
    updateItem(alarmItem.copy(isEnabled = isEnabled))
}

fun IAlarmStorage.findAlarmIds(
    hour: Int? = null,
    minute: Int? = null,
    is24HourView: Boolean? = null,
    isEnabled: Boolean? = null
): List<String> {
    val itemsList = Storage.getItems().mapNotNull { alarmItem ->
        val isValid = if (hour != null) alarmItem.hour == hour else true
                && if (minute != null) alarmItem.minute == minute else true
                && if (is24HourView != null) alarmItem.is24HourView == is24HourView else true
                && if (isEnabled != null) alarmItem.isEnabled == isEnabled else true
        if (isValid) alarmItem.id else null
    }.toList()

    return itemsList
}
