package com.dmytrokoniev.holyalarm

interface ITimeProvider {

    /**
     * Provides time in milliseconds.
     */
    fun provide(): Long
}
