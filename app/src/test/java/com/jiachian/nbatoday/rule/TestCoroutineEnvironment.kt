package com.jiachian.nbatoday.rule

import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestCoroutineEnvironment {
    val testScheduler = TestCoroutineScheduler()

    val testDispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
        override val io: CoroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
        override val default: CoroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
        override val unconfined: CoroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
    }

    val testScope = TestScope(testDispatcherProvider.unconfined)
}