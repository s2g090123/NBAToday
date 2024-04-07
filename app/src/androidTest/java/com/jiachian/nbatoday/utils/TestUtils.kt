package com.jiachian.nbatoday.utils

import androidx.navigation.NavController
import androidx.test.espresso.Espresso
import java.util.concurrent.CountDownLatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestScope
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`

inline fun <reified T : Any> T?.getOrAssert(errorMessage: () -> String? = { null }): T {
    return this ?: throw AssertionError(errorMessage() ?: "Object must not be null")
}

fun <T> T.assertIs(expected: T) = apply {
    assertThat(this, `is`(expected))
}

@Suppress("UNCHECKED_CAST")
fun <T, V : T> T.assertIsA(targetType: Class<V>): V {
    assertThat(this, instanceOf(targetType))
    return this as V
}

inline fun <reified T> Any?.assertIsA(): T {
    assertThat(this, instanceOf(T::class.java))
    return this as T
}

fun <T> T.assertIsNot(not: T) = apply {
    assertThat(this, not(not))
}

fun <T> T.assertIsNull() = apply {
    assertThat(this, nullValue())
}

inline fun <reified T : Any?> T?.assertIsNotNull() = run {
    assertThat(this, notNullValue())
    this as T
}

fun Boolean?.assertIsTrue() = apply {
    assertThat(this, `is`(true))
}

fun Boolean?.assertIsFalse() = apply {
    assertThat(this, `is`(false))
}

fun <T> T.assertIsTrue(runnable: (T) -> Boolean) = apply {
    assertThat(runnable(this), `is`(true))
}

fun <T> T.assertIsFalse(runnable: (T) -> Boolean) = apply {
    assertThat(runnable(this), `is`(false))
}

fun <T> assertIs(actual: T, expected: T) {
    assertThat(actual, `is`(expected))
}

fun <T> assertIsNot(actual: T, expected: T) {
    assertThat(actual, not(expected))
}

fun <T> assertIsA(actual: T, type: Class<*>?) {
    assertThat(actual, instanceOf(type))
}

fun <T> assertIsNull(actual: T) {
    assertThat(actual, nullValue())
}

fun <T> assertIsNotNull(actual: T) {
    assertThat(actual, notNullValue())
}

fun assertIsTrue(actual: Boolean?) {
    assertThat(actual, `is`(true))
}

fun assertIsFalse(actual: Boolean?) {
    assertThat(actual, `is`(false))
}

fun pressBack() {
    Espresso.pressBack()
}

fun NavController.assertCurrentRoute(expectedRouteName: String) {
    expectedRouteName.assertIs(currentBackStackEntry?.destination?.route)
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Flow<T>.collectOnce(
    scope: TestScope,
    collector: (T) -> Unit
) {
    val latch = CountDownLatch(1)
    val job = scope.async(Dispatchers.IO) {
        collect {
            try {
                collector(it)
            } finally {
                latch.countDown()
            }
        }
    }
    latch.await()
    job.cancelAndJoin()
}
