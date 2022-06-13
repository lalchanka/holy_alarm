package com.dmytrokoniev.holyalarm.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

abstract class SpStorage<T> : ISpStorage<T> {

    private val gson = Gson()
    private var context: Context? = null
    protected var sharedPreference: SharedPreferences? = null

    override fun initialize(context: Context) {
        this.context = context
        this.sharedPreference = context.getSharedPreferences(
            SP_FILE_NAME,
            Context.MODE_PRIVATE
        )
    }

    override fun dispose() {
        context = null
        sharedPreference = null
    }

    companion object {
        private const val SP_FILE_NAME = "alarms_data"
    }
}

enum class StorageType {
    ALARM_ITEM_STORAGE,
    LAST_ALARM_ID_STORAGE
}
