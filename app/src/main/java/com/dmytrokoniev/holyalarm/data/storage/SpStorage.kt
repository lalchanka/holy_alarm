package com.dmytrokoniev.holyalarm.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

abstract class SpStorage<T> : ISpStorage<T> {

    private var context: Context? = null
    protected val gson = Gson()
    protected var sharedPreference: SharedPreferences? = null
    protected abstract val spFileNameProvider: () -> String

    override fun initialize(context: Context) {
        this.context = context
        this.sharedPreference = context.getSharedPreferences(
            spFileNameProvider(),
            Context.MODE_PRIVATE
        )
    }

    override fun dispose() {
        context = null
        sharedPreference = null
    }
}

enum class StorageType {
    ALARM_ITEM_STORAGE,
    LAST_ALARM_ID_STORAGE
}
