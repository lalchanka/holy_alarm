package com.dmytrokoniev.holyalarm.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.dmytrokoniev.holyalarm.ui.AlarmItem
import com.google.gson.Gson

class SharedPreferencesAlarmStorage(
    context: Context
) : IAlarmStorage {

    private val sharedPreference = context.getSharedPreferences(SP_FILE_NAME, MODE_PRIVATE)
    private val gson = Gson()

    override fun addItem(item: AlarmItem) {
        val serializedItem = gson.toJson(item)
        sharedPreference.edit()
            .putString(item.id, serializedItem)
            .apply()
    }

    override fun getItems(): List<AlarmItem> {
        val strings = sharedPreference.all.toMap() as? Map<String, String> ?: emptyMap()
        return strings.map {
            gson.fromJson(it.value, AlarmItem::class.java)
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
        sharedPreference.edit()
            .remove(idToDelete)
            .apply()
        return true
    }

    override fun clear() {
        sharedPreference.edit()
            .clear()
            .apply()
    }

    companion object SPConstants {
        private const val SP_FILE_NAME = "alarms_data"
        private const val SP_ALARM_STRING_ID = "alarm_string"
    }
}
