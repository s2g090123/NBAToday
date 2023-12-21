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
