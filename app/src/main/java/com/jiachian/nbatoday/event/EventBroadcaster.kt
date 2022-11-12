package com.jiachian.nbatoday.event

import kotlinx.coroutines.flow.StateFlow

interface EventBroadcaster<T> {
    val eventFlow: StateFlow<T?>
    fun onEventConsumed(event: T?)
}