package com.dmytrokoniev.holyalarm.data.storage

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.dmytrokoniev.holyalarm.data.AlarmItem

typealias AlarmStorage = SpAlarmItemStorage

/*
    Class to work with AlarmItem DTO`s storage.
 */
@SuppressLint("StaticFieldLeak")
object SpAlarmItemStorage : SpStorage<AlarmItem>() {

    override val spFileNameProvider: () -> String
        get() = { "alarms_data" }

    override fun addItem(item: AlarmItem) {
        val serializedItem = gson.toJson(item)
        sharedPreference?.edit()
            ?.putString(item.id, serializedItem)
            ?.apply()
    }

    override fun getItems(): List<AlarmItem> {
        val strings = sharedPreference?.all?.toMap() as? Map<String, String> ?: emptyMap()
        return if (strings.isEmpty()) {
            emptyList()
        } else {
            strings.map {
                gson.fromJson(it.value, AlarmItem::class.java)
            }
        }
    }

    override fun updateItem(item: AlarmItem): Boolean {
        getItems().find { it.id == item.id } ?: return false
        deleteItem(item)
        addItem(item)
        return true
    }

    override fun deleteItem(item: AlarmItem): Boolean {
        getItems().find { it.id == item.id } ?: return false
        sharedPreference?.edit()
            ?.remove(item.id)
            ?.apply()
        return true
    }

    fun getStorage(): SharedPreferences? {
        return sharedPreference
    }
}

fun SpAlarmItemStorage.getItem(id: String): AlarmItem? = getItems().find { it.id == id }

fun SpAlarmItemStorage.addItems(items: List<AlarmItem>) = items.forEach { addItem(it) }

fun SpAlarmItemStorage.updateItemIsEnabled(id: String, isEnabled: Boolean) {
    val alarmItem = getItem(id) ?: return
    updateItem(alarmItem.copy(isEnabled = isEnabled))
}

fun SpAlarmItemStorage.findAlarmIds(
    hour: Int? = null,
    minute: Int? = null,
    is24HourView: Boolean? = null,
    isEnabled: Boolean? = null
): List<String> {
    val itemsList = getItems().mapNotNull { alarmItem ->
        val isValid = if (hour != null) alarmItem.hour == hour else true
                && if (minute != null) alarmItem.minute == minute else true
                && if (is24HourView != null) alarmItem.is24HourView == is24HourView else true
                && if (isEnabled != null) alarmItem.isEnabled == isEnabled else true
        if (isValid) alarmItem.id else null
    }.toList()

    return itemsList
}
