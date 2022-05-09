package com.dmytrokoniev.holyalarm.storage

import android.content.Context
import com.dmytrokoniev.holyalarm.ui.AlarmItem

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

//fun getIdList(): List<Int> = getItems().map { it.id }.toList()
//
//private fun List<AlarmItem>.containsAllIdsFrom(inputItems: List<AlarmItem>): Boolean {
//    inputItems.forEach { inputItem ->
//        this.find { currentItem -> currentItem.id == inputItem.id } ?: return false
//    }
//
//    return true
//}

//fun IAlarmStorage.updateItem(updatedItem: AlarmItem) = updateItems(listOf(updatedItem))
