package com.dmytrokoniev.holyalarm.data.storage

import android.content.Context

interface ISpStorage<T> : IStorage<T> {

    fun initialize(context: Context)

    fun dispose()
}
