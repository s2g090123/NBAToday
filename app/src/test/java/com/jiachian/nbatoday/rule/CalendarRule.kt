package com.jiachian.nbatoday.rule

import com.jiachian.nbatoday.BASIC_TIME
import com.jiachian.nbatoday.utils.NbaUtils
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.*

class CalendarRule : TestWatcher() {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                mockkObject(NbaUtils)
                every {
                    NbaUtils.getCalendar()
                } returns Calendar.getInstance().apply {
                    timeZone = TimeZone.getTimeZone("EST")
                    time = Date(BASIC_TIME)
                }
            }
        }
    }

    override fun finished(description: Description) {
        super.finished(description)
        unmockkObject(NbaUtils)
    }
}