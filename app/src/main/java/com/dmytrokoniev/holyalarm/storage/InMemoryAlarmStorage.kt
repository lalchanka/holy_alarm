package com.dmytrokoniev.holyalarm.storage

import com.dmytrokoniev.holyalarm.ui.AlarmItem

class InMemoryAlarmStorage: IAlarmStorage {

    private val alarmItems: MutableList<AlarmItem> = mutableListOf()

    override fun addItems(itemsList: List<AlarmItem>) {
        alarmItems.addAll(itemsList)
    }

    override fun getItems(): List<AlarmItem> = alarmItems
}


