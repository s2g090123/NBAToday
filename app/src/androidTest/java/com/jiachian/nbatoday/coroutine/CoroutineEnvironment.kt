package com.jiachian.nbatoday.coroutine

import com.jiachian.nbatoday.common.ui.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineEnvironment {

    val testScope = TestScope()

    val testScheduler = testScope.testScheduler

    val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    val testDispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher = testDispatcher
        override val io: CoroutineDispatcher = testDispatcher
        override val default: CoroutineDispatcher = testDispatcher
        override val unconfined: CoroutineDispatcher = testDispatcher
    }
}
