package com.jiachian.nbatoday.models.local.converter

import com.jiachian.nbatoday.database.converter.DateConverter
import java.util.Date
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class DateConverterTest {
    private val converter = DateConverter()

    @Test
    fun to_LongToDate_isCorrect() {
        val timestamp = 1672531200000L
        val expected = converter.to(timestamp)
        assertThat(expected, `is`(Date(timestamp)))
    }

    @Test
    fun from_DateToLong_isCorrect() {
        val timestamp = 1672531200000L
        val expected = converter.from(Date(timestamp))
        assertThat(expected, `is`(timestamp))
    }
}
