package com.jiachian.nbatoday.test.event

import com.jiachian.nbatoday.event.EventManager
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsNull
import org.junit.Before
import org.junit.Test

class EventManagerTest {
    sealed class Event {
        class TestEvent(val id: Long) : Event()
    }

    private lateinit var eventManger: EventManager<Event>

    @Before
    fun setup() {
        eventManger = EventManager()
    }

    @Test
    fun `send(TestEvent) expects eventFlow is updated`() {
        val expected = Event.TestEvent(0)
        eventManger.send(expected)
        val actual = eventManger.eventFlow.value
        assertIs(actual, expected)
    }

    @Test
    fun `onEventConsumed(TestEvent) expects event is consumed`() {
        val event = Event.TestEvent(0)
        eventManger.send(event)
        eventManger.onEventConsumed(event)
        val actual = eventManger.eventFlow.value
        assertIsNull(actual)
    }

    @Test
    fun `onEventConsumed(TestEvent) expects nextEvent is polled`() {
        val firstEvent = Event.TestEvent(0)
        val secondEvent = Event.TestEvent(1)
        eventManger.send(firstEvent)
        eventManger.send(secondEvent)
        eventManger.onEventConsumed(firstEvent)
        val actual = eventManger.eventFlow.value
        assertIs(actual, secondEvent)
    }

    @Test
    fun `onEventConsumed(null) expects nothing is changed`() {
        val event = Event.TestEvent(0)
        eventManger.send(event)
        eventManger.onEventConsumed(null)
        val actual = eventManger.eventFlow.value
        assertIs(actual, event)
    }

    @Test
    fun `onEventConsumed(notCurrent) expects nothing is changed`() {
        val event = Event.TestEvent(0)
        eventManger.send(event)
        eventManger.onEventConsumed(Event.TestEvent(1))
        val actual = eventManger.eventFlow.value
        assertIs(actual, event)
    }
}
