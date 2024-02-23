package com.jiachian.nbatoday.test.event

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.event.EventManager
import com.jiachian.nbatoday.utils.assertIs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EventManagerTest : BaseUnitTest() {
    sealed class Event {
        class TestEvent(val id: Long) : Event()
    }

    private lateinit var eventManger: EventManager<Event>

    @Before
    fun setup() {
        eventManger = EventManager()
    }

    @Test
    fun `send(TestEvent) expects eventFlow is updated`() = launch {
        val expected = Event.TestEvent(0)
        val event = eventManger.eventFlow.defer(this)
        eventManger.send(expected)
        event
            .await()
            .assertIs(expected)
    }
}
