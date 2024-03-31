package com.jiachian.nbatoday.test.database.converter

import com.jiachian.nbatoday.common.data.database.converter.DateConverter
import com.jiachian.nbatoday.utils.assertIs
import org.junit.Test
import java.sql.Date

class DateConverterTest {
    private val converter = DateConverter()

    @Test
    fun to_LongToDate_isCorrect() {
        val timestamp = 1672531200000L
        val expected = converter.to(timestamp)
        assertIs(expected, Date(timestamp))
    }

    @Test
    fun from_DateToLong_isCorrect() {
        val timestamp = 1672531200000L
        val expected = converter.from(Date(timestamp))
        assertIs(expected, timestamp)
    }
}
