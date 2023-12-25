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

fun <T> T.assertIsA(type: Class<*>?) = apply {
    assertThat(this, instanceOf(type))
}

fun <T> T.assertIsNot(not: T) = apply {
    assertThat(this, not(not))
}

fun <T> T.assertIsNull() = apply {
    assertThat(this, nullValue())
}

fun <T> T.assertIsNotNull() = apply {
    assertThat(this, notNullValue())
}

fun Boolean?.assertIsTrue() = apply {
    assertThat(this, `is`(true))
}

fun Boolean?.assertIsFalse() = apply {
    assertThat(this, `is`(false))
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
