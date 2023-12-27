package com.jiachian.nbatoday.rule

import com.jiachian.nbatoday.coroutine.CoroutineEnvironment
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineRule : TestWatcher() {
    lateinit var coroutineEnvironment: CoroutineEnvironment
        private set

    val dispatcherProvider: DispatcherProvider
        get() = coroutineEnvironment.testDispatcherProvider

    override fun starting(description: Description) {
        coroutineEnvironment = CoroutineEnvironment()
    }

    override fun finished(description: Description) {
        coroutineEnvironment.testScope.cancel()
    }

    fun launch(testBody: suspend TestScope.() -> Unit) = coroutineEnvironment.testScope.runTest {
        testBody()
    }
}
