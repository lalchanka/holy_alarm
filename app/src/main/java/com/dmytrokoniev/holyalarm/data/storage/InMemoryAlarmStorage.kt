package com.dmytrokoniev.holyalarm.data.storage

import android.content.Context
import com.dmytrokoniev.holyalarm.data.AlarmItem

class InMemoryAlarmStorage: IAlarmStorage {

    private val alarmItems: MutableList<AlarmItem> = mutableListOf()

    override fun initialize(context: Context) {
        TODO("Not yet implemented")
    }

    override fun addItem(item: AlarmItem) {
        alarmItems.add(item)
    }

    override fun getItem(id: String): AlarmItem? {
        TODO("Not yet implemented")
    }

    override fun getItems(): List<AlarmItem> = alarmItems

    override fun updateItem(item: AlarmItem): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteItem(idToDelete: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }
}


