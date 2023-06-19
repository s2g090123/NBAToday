package com.jiachian.nbatoday.rule

import com.jiachian.nbatoday.BASIC_TIME
import com.jiachian.nbatoday.utils.NbaUtils
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class CalendarRule : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        mockkObject(NbaUtils)
        every {
            NbaUtils.getCalendar()
        } answers {
            Calendar.getInstance().apply {
                timeZone = TimeZone.getTimeZone("EST")
                time = Date(BASIC_TIME)
            }
        }
    }

    override fun finished(description: Description) {
        super.finished(description)
        unmockkObject(NbaUtils)
    }
}