package com.dmytrokoniev.holyalarm.storage

data class AlarmModel(
    val id: Int,
    val hour: Int,
    val minute: Int,
    val is24HourView: Boolean,
    val isEnabled: Boolean
)
