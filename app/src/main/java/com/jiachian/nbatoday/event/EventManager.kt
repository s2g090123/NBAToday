package com.jiachian.nbatoday.event

import androidx.annotation.VisibleForTesting
import com.jiachian.nbatoday.annotation.ExcludeFromJacocoGeneratedReport
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Manages and broadcasts events of type [T].
 *
 * @param T The type of events to be managed.
 */
class EventManager<T> : EventBroadcaster<T> {
    private val eventFlowImp = MutableSharedFlow<T>(
        replay = 0,
        extraBufferCapacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    override val eventFlow = eventFlowImp.asSharedFlow()

    /**
     * Sends an event to be broadcast. If there is no ongoing event, it is immediately broadcast.
     * Otherwise, the event is added to the buffer.
     *
     * @param event The event to be broadcast.
     */
    @Synchronized
    fun send(event: T) {
        eventFlowImp.tryEmit(event)
    }

    @VisibleForTesting
    @ExcludeFromJacocoGeneratedReport
    @OptIn(ExperimentalCoroutinesApi::class)
    fun reset() {
        eventFlowImp.resetReplayCache()
    }
}
