package com.dmytrokoniev.holyalarm.data.storage

/*
    Class to work with Int values storage.
    TODO: Implement member functions (konevdmytro)
 */
class SpLastAlarmIdStorage : SpStorage<Int>() {

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

    companion object {
        private const val LAST_ALARM_ID_FIELD = "last_id"
    }
}
