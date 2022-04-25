package com.dmytrokoniev.holyalarm.storage

import android.content.Context
import com.dmytrokoniev.holyalarm.ui.AlarmItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesAlarmStorage(
    context: Context
): IAlarmStorage {

    private val sharedPreference = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun addItems(itemsList: List<AlarmItem>) {
        val string = gson.toJson(itemsList)
        sharedPreference
            .edit()
            .putString(SP_ALARM_STRING_ID, string)
            .apply()
    }

    override fun getItems(): List<AlarmItem> {
        val string = sharedPreference.getString(SP_ALARM_STRING_ID, "")
        return if (string != null && string.isNotEmpty()) {
            val itemType = object : TypeToken<List<AlarmItem>>() {}.type
            gson.fromJson(string, itemType)
        } else {
            emptyList()
        }
    }

    override fun updateItems(itemsList: List<AlarmItem>): Boolean {
        val mutableList = getItems().toMutableList()
        val idList = mutableList.map { it.id }.toList()
        val resultList = emptyList<AlarmItem>().toMutableList()
        for (item in mutableList) {
            if (idList.contains(item.id)) {
                val updatedItem = itemsList.find { it.id == item.id }
                updatedItem?.let {
                    resultList.add(updatedItem)
                }
            } else {
                resultList.add(item)
            }
        }

        addItems(resultList.toList())

        return true
    }

//    override fun updateItems(itemsList: List<AlarmItem>): Boolean {
//        val currentItems = getItems()
//        currentItems.containsAllIdsFrom(itemsList)
//        val updatedItems = currentItems.map { currentItem ->
//            itemsList.find { it.id == currentItem.id } ?: currentItem
//        }.toList()
//
//        addItems(updatedItems)
//
//        return true
//    }

    private fun List<AlarmItem>.containsAllIdsFrom(inputItems: List<AlarmItem>): Boolean {
        inputItems.forEach { inputItem ->
            this.find { currentItem -> currentItem.id == inputItem.id } ?: return false
        }

        return true
    }

    override fun deleteItems(itemsList: List<Int>) {
        val mutableList = getItems().toMutableList()
        val resultList = emptyList<AlarmItem>().toMutableList()
        for (item in mutableList) {
            if (!itemsList.contains(item.id)) resultList.add(item)
        }

        addItems(resultList.toList())
    }

    override fun clear() {
        sharedPreference
            .edit()
            .remove(SP_ALARM_STRING_ID)
            .apply()
    }

    fun getIdList(): List<Int> = getItems().map { it.id }.toList()

    companion object SPConstants {
        private const val SP_FILE_NAME = "alarms_data"
        private const val SP_ALARM_STRING_ID = "alarm_string"
    }
}
