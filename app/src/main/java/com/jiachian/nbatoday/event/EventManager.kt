package com.jiachian.nbatoday.event

import androidx.annotation.VisibleForTesting
import com.jiachian.nbatoday.annotation.ExcludeFromJacocoGeneratedReport
import java.util.concurrent.ConcurrentLinkedQueue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Manages and broadcasts events of type [T].
 *
 * @param T The type of events to be managed.
 */
class EventManager<T> : EventBroadcaster<T> {
    private val queue = ConcurrentLinkedQueue<T>()
    private val eventFlowImp = MutableStateFlow<T?>(null)
    override val eventFlow: StateFlow<T?> = eventFlowImp
    private var currentEvent: T? = null

    /**
     * Sends an event to be broadcast. If there is no ongoing event, it is immediately broadcast.
     * Otherwise, the event is added to the queue.
     *
     * @param event The event to be broadcast.
     */
    @Synchronized
    fun send(event: T) {
        if (currentEvent == null) {
            currentEvent = event
            eventFlowImp.value = currentEvent
        } else {
            queue.offer(event)
        }
    }

    /**
     * Callback method to be invoked when an event is consumed. It updates the current event and
     * broadcasts the next event from the queue if available.
     *
     * @param event The event that was consumed.
     */
    @Synchronized
    override fun onEventConsumed(event: T?) {
        if (event == null || event != currentEvent) return
        eventFlowImp.value = queue.poll().also {
            currentEvent = it
        }
    }

    @VisibleForTesting
    @ExcludeFromJacocoGeneratedReport
    fun reset() {
        queue.clear()
        currentEvent = null
        eventFlowImp.value = null
    }
}
