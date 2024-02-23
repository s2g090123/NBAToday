package com.jiachian.nbatoday.test.event

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.event.ToastEvent
import com.jiachian.nbatoday.event.send
import com.jiachian.nbatoday.event.toastEventManager
import com.jiachian.nbatoday.utils.assertIs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ToastEventTest : BaseUnitTest() {
    @After
    fun teardown() {
        toastEventManager.reset()
    }

    @Test
    fun `send() expects event is in EventManger`() = launch {
        val event = toastEventManager.eventFlow.defer(this)
        ToastEvent.OnError.send()
        event
            .await()
            .assertIs(ToastEvent.OnError)
    }
}
