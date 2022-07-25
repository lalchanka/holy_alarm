package com.dmytrokoniev.holyalarm.util

import androidx.recyclerview.widget.ItemTouchHelper

class AlarmItemTouchHelper : ItemTouchHelper(
    object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT)
) {
}
