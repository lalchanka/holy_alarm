package com.dmytrokoniev.holyalarm.storage

import com.dmytrokoniev.holyalarm.ui.AlarmItem

interface IAlarmStorage {

    fun addItem(item: AlarmItem)

    fun getItems(): List<AlarmItem>

    fun updateItem(item: AlarmItem): Boolean

    fun deleteItem(idToDelete: String): Boolean

    fun clear()
}

fun IAlarmStorage.addItems(items: List<AlarmItem>) = items.forEach { addItem(it) }

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
