package com.dmytrokoniev.holyalarm.storage

import android.content.Context
import android.content.SharedPreferences
import com.dmytrokoniev.holyalarm.ui.AlarmItem
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class SharedPreferencesAlarmStorage(
    context: Context
): IAlarmStorage {

    private val sharedPreference = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE)

    override fun addItems(itemsList: List<AlarmItem>) {
        // 13.01.2022 dmytrokoniev@gmail.com TODO: <text of todo>
//        val editor = sharedPreference.edit()
        val stringSet = itemsList.map { Gson().toJson(it) }.toSet()
        sharedPreference
            .edit()
            .putStringSet(SP_ALARM_STRING_SET_ID, stringSet)
            .commit()
//        editor.putStringSet(SP_ALARM_STRING_SET_ID, stringSet)
//        editor.commit()
    }

    override fun getItems(): List<AlarmItem> {
        // 13.01.2022 dmytrokoniev@gmail.com TODO: <text of todo>
        val stringSet = sharedPreference.getStringSet(SP_ALARM_STRING_SET_ID, emptySet())
        val mutableList = emptyList<AlarmItem>().toMutableList()
        stringSet?.let {
            for (index in stringSet.indices) {
                mutableList.add(
                    Gson().fromJson(stringSet.elementAt(index), AlarmItem::class.java)
                )
            }
        }

        return mutableList.toList()
    }

    companion object SPConstants {
        private const val SP_FILE_NAME = "alarms_data"
        private const val SP_ALARM_STRING_SET_ID = "alarm_string_set"
    }
}
