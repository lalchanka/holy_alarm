package com.dmytrokoniev.holyalarm.storage

import com.dmytrokoniev.holyalarm.ui.AlarmItem

interface IAlarmStorage {

    fun addItems(itemsList: List<AlarmItem>)

    fun getItems(): List<AlarmItem>

//    fun updateItems(itemsList: List<AlarmItem>)
//
//    fun deleteItems(itemsList: List<Int>)
//
//    fun clear()
}

fun IAlarmStorage.addItem(newItem: AlarmItem) = addItems(listOf(newItem))

fun IAlarmStorage.getItem(index: Int) = getItems()[index]

//fun IAlarmStorage.updateItem(updatedItem: AlarmItem) = updateItems(listOf(updatedItem))
