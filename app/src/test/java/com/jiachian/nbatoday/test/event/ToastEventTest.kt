package com.jiachian.nbatoday.test.event

import com.jiachian.nbatoday.event.ToastEvent
import com.jiachian.nbatoday.event.consume
import com.jiachian.nbatoday.event.send
import com.jiachian.nbatoday.event.toastEventManager
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsNull
import org.junit.After
import org.junit.Test

class ToastEventTest {
    @After
    fun teardown() {
        toastEventManager.reset()
    }

    @Test
    fun `send() expects event is in EventManger`() {
        val event = ToastEvent.OnError
        event.send()
        val actual = toastEventManager.eventFlow.value
        assertIs(actual, event)
    }

    @Test
    fun `consume() expects event is consumed`() {
        val event = ToastEvent.OnError
        event.send()
        event.consume()
        val actual = toastEventManager.eventFlow.value
        assertIsNull(actual)
    }
}
