package com.dmytrokoniev.holyalarm.data.storage

import android.annotation.SuppressLint
import android.content.SharedPreferences

typealias LastIdStorage = SpLastAlarmIdStorage

/**
 * Class to work with LastAlarmId storage.
 */
@SuppressLint("StaticFieldLeak")
object SpLastAlarmIdStorage : SpStorage<Int>() {

    private const val LAST_ALARM_ID_FIELD = "last_id"
    override val spFileNameProvider: () -> String
        get() = { "lastAlarmId_data" }

    override fun addItem(item: Int) {
        sharedPreference?.edit()
            ?.putInt(LAST_ALARM_ID_FIELD, item)
            ?.apply()
    }

    override fun getItems(): List<Int> {
        val lastId = sharedPreference?.getInt(LAST_ALARM_ID_FIELD, 0)
        return if (lastId == null) {
            emptyList()
        } else {
            listOf(lastId)
        }
    }

    override fun updateItem(item: Int): Boolean {
        val lastId = getItems()
        return if (lastId.isEmpty()) {
            false
        } else {
            addItem(item)
            true
        }
    }

    override fun deleteItem(item: Int): Boolean {
//        sharedPreference?.edit()
//            ?.remove(LAST_ALARM_ID_FIELD)
//            ?.apply()
        return true
    }

    fun getStorage(): SharedPreferences? {
        return sharedPreference
    }
}

fun SpLastAlarmIdStorage.getLastId(): Int {
    val listId = getItems()
    return if (listId.isEmpty()) {
        addItem(item = 0)
        0
    } else {
        listId[0]
    }
}

fun SpLastAlarmIdStorage.setLastId(newId: Int) = addItem(newId)
