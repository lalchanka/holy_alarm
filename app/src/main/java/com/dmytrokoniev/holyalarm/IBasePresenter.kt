package com.dmytrokoniev.holyalarm

import kotlinx.coroutines.CoroutineScope

interface IBasePresenter {

    fun initialize(scope: CoroutineScope)

    fun dispose()
}
