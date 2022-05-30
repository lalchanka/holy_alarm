package com.dmytrokoniev.holyalarm.data.storage

interface ISpStorage<T> : IStorage<T> {

    fun initialize()

    fun dispose()
}
