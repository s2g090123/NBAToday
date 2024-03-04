package com.jiachian.nbatoday.rule

import com.jiachian.nbatoday.BasicTime
import com.jiachian.nbatoday.utils.DateUtils
import io.mockk.every
import io.mockk.mockkObject
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class CalendarRule : TestWatcher() {
    override fun starting(description: Description) {
        mockkObject(DateUtils)
        every {
            DateUtils.getCalendar()
        } answers {
            Calendar.getInstance().apply {
                timeZone = TimeZone.getTimeZone("EST")
                time = Date(BasicTime)
            }
        }
    }

    override fun finished(description: Description) {
        /**
         * WORKAROUND!
         * When dealing with #52-collectAsStateWithLifecycle, upgraded the android gradle plugin and gradle versions,
         * and then started encountering the `io.mockk.MockKException: can't find stub DateUtils(object DateUtils)` problem.
         *
         * Unable to confirm the exact cause of the issue,
         * but suspect it may be related to issues with unmock.
         * Therefore, commented out the following code for now.
         */
        //unmockkObject(DateUtils)
    }
}
