package com.jiachian.nbatoday.rule

import com.jiachian.nbatoday.coroutine.CoroutineEnvironment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class SetMainDispatcherRule(
    private val coroutineEnvironment: CoroutineEnvironment
) : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(coroutineEnvironment.testDispatcherProvider.main)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
