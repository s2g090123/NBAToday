package com.jiachian.nbatoday.event

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class EventManagerTest {
    private sealed class Event {
        class EventClass : Event()
        class EventClassWithParameter(val parameter: Any) : Event()
        object EventObject : Event()
    }

    private lateinit var eventManager: EventManager<Event>

    @Before
    fun setup() {
        eventManager = EventManager()
    }

    @Test
    fun send_sendEventClass_checksEventFlowCorrect() {
        val event = Event.EventClass()
        eventManager.send(event)
        assertThat(eventManager.eventFlow.value, instanceOf(Event.EventClass::class.java))
        assertThat(eventManager.eventFlow.value, `is`(event))
    }

    @Test
    fun send_sendEventClassWithParameter_checksEventFlowCorrect() {
        val parameter = "test"
        val event = Event.EventClassWithParameter(parameter)
        eventManager.send(event)
        assertThat(
            eventManager.eventFlow.value,
            instanceOf(Event.EventClassWithParameter::class.java)
        )
        assertThat(eventManager.eventFlow.value, `is`(event))
        assertThat(
            (eventManager.eventFlow.value as Event.EventClassWithParameter).parameter,
            `is`(parameter)
        )
    }

    @Test
    fun send_sendEventObject_checksEventFlowCorrect() {
        val event = Event.EventObject
        eventManager.send(event)
        assertThat(eventManager.eventFlow.value, instanceOf(Event.EventObject::class.java))
        assertThat(eventManager.eventFlow.value, `is`(event))
    }

    @Test
    fun send_sendEventTwice_checksCurrentEventIsFirstEvent() {
        val firstEvent = Event.EventClass()
        val secondEvent = Event.EventObject
        eventManager.send(firstEvent)
        eventManager.send(secondEvent)
        assertThat(eventManager.eventFlow.value, instanceOf(Event.EventClass::class.java))
        assertThat(eventManager.eventFlow.value, `is`(firstEvent))
    }

    @Test
    fun onEventConsumed_consumeEvent_checksEventConsumed() {
        eventManager.send(Event.EventObject)
        assertThat(eventManager.eventFlow.value, `is`(Event.EventObject))
        eventManager.onEventConsumed(Event.EventObject)
        assertThat(eventManager.eventFlow.value, nullValue())
    }

    @Test
    fun onEventConsumed_consumeFirstEvent_checksEventIsSecondEvent() {
        val firstEvent = Event.EventClass()
        val secondEvent = Event.EventObject
        eventManager.send(firstEvent)
        eventManager.send(secondEvent)
        assertThat(eventManager.eventFlow.value, `is`(firstEvent))
        eventManager.onEventConsumed(firstEvent)
        assertThat(eventManager.eventFlow.value, `is`(secondEvent))
    }

    @Test
    fun onEventConsumed_consumeNull_checksEventFlowNotChanged() {
        eventManager.send(Event.EventObject)
        assertThat(eventManager.eventFlow.value, `is`(Event.EventObject))
        eventManager.onEventConsumed(null)
        assertThat(eventManager.eventFlow.value, `is`(Event.EventObject))
    }

    @Test
    fun onEventConsumed_consumeNotCurrentEvent_checksEventFlowNotChanged() {
        eventManager.send(Event.EventObject)
        assertThat(eventManager.eventFlow.value, `is`(Event.EventObject))
        eventManager.onEventConsumed(Event.EventClass())
        assertThat(eventManager.eventFlow.value, `is`(Event.EventObject))
    }
}
