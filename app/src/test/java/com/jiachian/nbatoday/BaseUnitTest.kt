package com.jiachian.nbatoday

import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.rule.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
open class BaseUnitTest {
    @get:Rule
    val coroutineRule = CoroutineRule()

    val dispatcherProvider: DispatcherProvider
        get() = coroutineRule.dispatcherProvider

    fun launch(testBody: suspend TestScope.() -> Unit) = coroutineRule.launch {
        testBody()
    }
}
