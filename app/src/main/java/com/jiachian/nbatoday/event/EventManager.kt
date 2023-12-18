package com.jiachian.nbatoday.event

import java.util.concurrent.ConcurrentLinkedQueue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventManager<T> : EventBroadcaster<T> {
    private val queue = ConcurrentLinkedQueue<T>()
    private val eventFlowImp = MutableStateFlow<T?>(null)
    override val eventFlow: StateFlow<T?> = eventFlowImp
    private var currentEvent: T? = null

    @Synchronized
    fun send(event: T) {
        if (currentEvent == null) {
            currentEvent = event
            eventFlowImp.value = currentEvent
        } else {
            queue.offer(event)
        }
    }

    @Synchronized
    override fun onEventConsumed(event: T?) {
        if (event == null || event != currentEvent) return
        eventFlowImp.value = queue.poll().also {
            currentEvent = it
        }
    }
}
