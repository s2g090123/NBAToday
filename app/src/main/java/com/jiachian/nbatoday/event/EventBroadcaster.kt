package com.jiachian.nbatoday.event

import kotlinx.coroutines.flow.SharedFlow

interface EventBroadcaster<T> {
    val eventFlow: SharedFlow<T>
}
