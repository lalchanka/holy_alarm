package com.dmytrokoniev.holyalarm.bus

sealed interface UiEvent

sealed interface StopAlarmFragmentEvent : UiEvent {

    object StopClicked : StopAlarmFragmentEvent
}

sealed interface AlarmItemViewHolderEvent : UiEvent {

    object AlarmOn : AlarmItemViewHolderEvent

    object AlarmOff : AlarmItemViewHolderEvent

    object AlarmSet : AlarmItemViewHolderEvent
}

sealed interface AlarmListFragmentEvent : UiEvent {

    object AddClicked : AlarmListFragmentEvent
}

sealed interface ToolbarEvent : UiEvent {

    object ConfirmClicked : ToolbarEvent

    object CancelClicked : ToolbarEvent
}
