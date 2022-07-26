package com.dmytrokoniev.holyalarm

import kotlinx.coroutines.CoroutineScope

interface IBasePresenter {

    fun initialize(coroutineScope: CoroutineScope)

    fun dispose()
}
