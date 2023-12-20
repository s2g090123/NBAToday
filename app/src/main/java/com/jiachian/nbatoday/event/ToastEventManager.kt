package com.jiachian.nbatoday.event

sealed class ToastEvent {
    object onError : ToastEvent()
}

val toastEventManager = EventManager<ToastEvent>()

fun ToastEvent.send() {
    toastEventManager.send(this)
}

fun ToastEvent?.consume() {
    toastEventManager.onEventConsumed(this)
}
