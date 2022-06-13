package com.dmytrokoniev.holyalarm.data

interface LeSortierStandart<T> {
    
    fun sortAscending(items: Iterable<T>): Iterable<T> 
}

class SortierStandart : LeSortierStandart<AlarmItem> {

    override fun sortAscending(items: Iterable<AlarmItem>): Iterable<AlarmItem> {
        TODO("Not yet implemented")
    }

}
