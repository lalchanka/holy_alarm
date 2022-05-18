package com.dmytrokoniev.holyalarm.bus

import kotlinx.coroutines.channels.ConflatedBroadcastChannel

object AppStateBus {

    private var appStatePipeline: ConflatedBroadcastChannel<AppState>? = null

    fun initialize() {
        val initialState = AppState(isAlarmUpdateFlow = false)
        appStatePipeline = ConflatedBroadcastChannel(initialState)
    }

    /**
     * Sends [AppState]s only while the [AppStateBus] is alive. This means that you should use
     * the [onSendAppState] only After the [initialize] and Before the [dispose] function calls.
     */
    suspend fun onSendAppState(appState: AppState) {
        appStatePipeline?.send(appState)
    }

    /**
     * Receives [AppState]s only while the [AppStateBus] is alive. This means that you should use
     * the [onReceiveAppState] only After the [initialize] and Before the [dispose] function calls.
     * Otherwise [IllegalStateException] will be thrown.
     */
    suspend fun onReceiveAppState(): AppState = requireReceive()

    fun dispose() {
        appStatePipeline?.close()
        appStatePipeline = null
    }

    private suspend fun requireReceive(): AppState =
        appStatePipeline?.openSubscription()?.receive() ?: throw IllegalStateException(
            "Calling onReceiveAppState outside of AppStateBus lifecycle"
        )
}

data class AppState(
    val isAlarmUpdateFlow: Boolean
)
