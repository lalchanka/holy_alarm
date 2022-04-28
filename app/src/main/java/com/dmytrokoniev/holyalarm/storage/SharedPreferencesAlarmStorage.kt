package com.dmytrokoniev.holyalarm.storage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.dmytrokoniev.holyalarm.ui.AlarmItem
import com.google.gson.Gson

@SuppressLint("StaticFieldLeak")
object SharedPreferencesAlarmStorage : IAlarmStorage {

    private const val SP_FILE_NAME = "alarms_data"
    private val gson = Gson()
    private var context: Context? = null
    private var sharedPreference: SharedPreferences? = null

    override fun initialize(context: Context) {
        this.context = context
        this.sharedPreference = context.getSharedPreferences(SP_FILE_NAME, MODE_PRIVATE)
    }

    override fun addItem(item: AlarmItem) {
        val serializedItem = gson.toJson(item)
        sharedPreference?.edit()
            ?.putString(item.id, serializedItem)
            ?.apply()
    }

    override fun getItem(id: String): AlarmItem? {
        val serializedItem = sharedPreference?.getString(id, null)
        return if (serializedItem != null) {
            gson.fromJson(serializedItem, AlarmItem::class.java)
        } else {
            null
        }
    }

    override fun getItems(): List<AlarmItem> {
        val strings = sharedPreference?.all?.toMap() as? Map<String, String> ?: emptyMap()
        return if (strings.isEmpty()) {
            emptyList()
        } else {
            strings.map {
                gson.fromJson(it.value, AlarmItem::class.java)
            }
        }
    }

    override fun updateItem(item: AlarmItem): Boolean {
        getItems().find { it.id == item.id } ?: return false
        deleteItem(item.id)
        addItem(item)
        return true
    }

    override fun deleteItem(idToDelete: String): Boolean {
        getItems().find { it.id == idToDelete } ?: return false
        sharedPreference?.edit()
            ?.remove(idToDelete)
            ?.apply()
        return true
    }

    override fun clear() {
        sharedPreference?.edit()
            ?.clear()
            ?.apply()
    }

    override fun dispose() {
        context = null
        sharedPreference = null
    }
}
