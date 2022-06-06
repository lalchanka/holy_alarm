package com.dmytrokoniev.holyalarm.data

interface LeSortierStandart<T> {
    
    fun sortAscending(items: Iterable<T>): Iterable<T> 
}

class SortierStandart : LeSortierStandart<AlarmItem> {
    
}
