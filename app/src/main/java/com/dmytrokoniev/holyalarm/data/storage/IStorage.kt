package com.dmytrokoniev.holyalarm.data.storage

interface IStorage<T> {

    fun addItem(item: T)

    fun getItems(): List<T>

    fun updateItem(item: T): Boolean

    fun deleteItem(item: T): Boolean
}
