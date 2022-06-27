package com.dmytrokoniev.holyalarm.data.storage

import android.content.Context

class StorageFabric {

//    fun <T> createFromStorageType(storageType: StorageType, context: Context): SpStorage<T> {
//        return when (storageType) {
//            StorageType.ALARM_ITEM_STORAGE -> SpAlarmItemStorage
//            StorageType.LAST_ALARM_ID_STORAGE -> SpLastAlarmIdStorage()
//        }
//    }
}

enum class StorageType {
    ALARM_ITEM_STORAGE,
    LAST_ALARM_ID_STORAGE
}
