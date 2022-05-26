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
