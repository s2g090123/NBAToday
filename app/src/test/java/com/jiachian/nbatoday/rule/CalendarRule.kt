package com.jiachian.nbatoday.rule

import com.jiachian.nbatoday.BasicTime
import com.jiachian.nbatoday.utils.DateUtils
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class CalendarRule : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        mockkObject(DateUtils) {
            every {
                DateUtils.getCalendar()
            } answers {
                Calendar.getInstance().apply {
                    timeZone = TimeZone.getTimeZone("EST")
                    time = Date(BasicTime)
                }
            }
        }
    }

    override fun finished(description: Description) {
        super.finished(description)
        unmockkObject(DateUtils)
    }
}
