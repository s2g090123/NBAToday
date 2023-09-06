package com.jiachian.nbatoday.utils

import com.jiachian.nbatoday.rule.TestCoroutineEnvironment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> Flow<T>.launchAndCollect(coroutineEnvironment: TestCoroutineEnvironment): Job {
    return CoroutineScope(coroutineEnvironment.testDispatcherProvider.unconfined).launch {
        collect()
    }
}

inline fun <reified T : Any> T?.getOrAssert(errorMessage: () -> String? = { null }): T {
    return this ?: throw AssertionError(errorMessage() ?: "Object must not be null")
}