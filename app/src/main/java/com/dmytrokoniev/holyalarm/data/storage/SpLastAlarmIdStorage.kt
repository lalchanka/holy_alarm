package com.dmytrokoniev.holyalarm.data.storage

import android.annotation.SuppressLint

// TODO: Implement member functions (konevdmytro)
/**
 * Class to work with Int values storage.
 */
@SuppressLint("StaticFieldLeak")
object SpLastAlarmIdStorage : SpStorage<Int>() {

    private const val LAST_ALARM_ID_FIELD = "last_id"

    override fun addItem(item: Int) {
        TODO("Not yet implemented")
    }

    override fun getItems(): List<Int> {
        TODO("Not yet implemented")
    }

    override fun updateItem(item: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteItem(item: Int): Boolean {
        TODO("Not yet implemented")
    }
}

// TODO Put this code to getItems function (konevdmytro)
fun SpLastAlarmIdStorage.getLastId(): Int {
    TODO()
//    val lastId = SharedPreferencesAlarmStorage.sharedPreference?.getInt(
//        SharedPreferencesAlarmStorage.LAST_ALARM_ID_FIELD, 0)
//    return lastId ?: 0
}

// TODO Put this code to updateItems function (konevdmytro)
fun SpLastAlarmIdStorage.setLastId(newId: Int) {
    TODO()
//    SharedPreferencesAlarmStorage.deleteItem(SharedPreferencesAlarmStorage.LAST_ALARM_ID_FIELD)
//    SharedPreferencesAlarmStorage.sharedPreference?.edit()
//        ?.putInt(SharedPreferencesAlarmStorage.LAST_ALARM_ID_FIELD, newId)
//        ?.apply()
}
