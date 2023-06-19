package com.jiachian.nbatoday.utils

import com.jiachian.nbatoday.rule.TestCoroutineEnvironment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.launchAndCollect(coroutineEnvironment: TestCoroutineEnvironment) {
    coroutineEnvironment.testScope.launch(coroutineEnvironment.testDispatcherProvider.unconfined) {
        collect()
    }
}