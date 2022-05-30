package com.dmytrokoniev.holyalarm.data.storage

import com.dmytrokoniev.holyalarm.data.AlarmItem

interface IStorage<T> {

    fun addItem(item: T)

    fun getItems(): List<T>

    fun updateItem(item: T): Boolean

    fun deleteItem(item: T): Boolean
}

fun <T> IStorage<T>.getItem(id: String): T {
    TODO()
}
