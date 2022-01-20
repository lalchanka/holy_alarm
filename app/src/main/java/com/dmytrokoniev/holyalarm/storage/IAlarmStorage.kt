package com.dmytrokoniev.holyalarm.storage

import com.dmytrokoniev.holyalarm.ui.AlarmItem

interface IAlarmStorage {

    fun addItems(itemsList: List<AlarmItem>)

    fun getItems(): List<AlarmItem>
}

fun IAlarmStorage.addItem(newItem: AlarmItem) = addItems(listOf(newItem))
