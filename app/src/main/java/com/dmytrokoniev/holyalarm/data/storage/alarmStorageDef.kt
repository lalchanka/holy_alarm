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
