package com.jiachian.nbatoday.utils

import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`

inline fun <reified T : Any> T?.getOrAssert(errorMessage: () -> String? = { null }): T {
    return this ?: throw AssertionError(errorMessage() ?: "Object must not be null")
}

fun <T> assertIs(actual: T, expected: T) {
    assertThat(actual, `is`(expected))
}

fun <T> assertIsA(actual: T, type: Class<*>?) {
    assertThat(actual, instanceOf(type))
}
