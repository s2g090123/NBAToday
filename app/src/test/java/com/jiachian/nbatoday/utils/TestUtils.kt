package com.jiachian.nbatoday.utils

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

fun <T> T?.assertIsNull() = apply {
    assertThat(this, nullValue())
}

fun <T> T?.assertIsNotNull() = let {
    assertThat(this, notNullValue())
    it as T
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
