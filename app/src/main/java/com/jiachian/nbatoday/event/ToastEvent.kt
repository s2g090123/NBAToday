package com.jiachian.nbatoday.event

sealed class ToastEvent {
    object OnError : ToastEvent()
}

val toastEventManager = EventManager<ToastEvent>()

fun ToastEvent.send() {
    toastEventManager.send(this)
}
