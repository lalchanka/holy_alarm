package com.dmytrokoniev.holyalarm.data

interface LeSortierStandart<T> {
    
    fun sortAscending(items: Iterable<T>): Iterable<T> 
}

object SortierStandart : LeSortierStandart<AlarmItem> {

    override fun sortAscending(items: Iterable<AlarmItem>): Iterable<AlarmItem> =
        items.sortedBy { it.id.toInt() }
}
