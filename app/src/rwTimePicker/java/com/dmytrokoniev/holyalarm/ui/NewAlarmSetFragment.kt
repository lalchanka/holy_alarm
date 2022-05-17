package com.dmytrokoniev.holyalarm.ui

import kotlin.random.Random

class NewAlarmSetFragment : AlarmSetFragment() {

    override val alarmIdProvider: () -> String
        get() = { Random.nextInt().toString() }
}
