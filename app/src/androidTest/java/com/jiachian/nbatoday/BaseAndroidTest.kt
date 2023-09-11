package com.jiachian.nbatoday

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import com.jiachian.nbatoday.rule.CalendarRule
import com.jiachian.nbatoday.rule.TestCoroutineEnvironment
import org.junit.Rule

open class BaseAndroidTest {
    protected val coroutineEnvironment = TestCoroutineEnvironment()

    protected val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val calendarRule = CalendarRule()
}
