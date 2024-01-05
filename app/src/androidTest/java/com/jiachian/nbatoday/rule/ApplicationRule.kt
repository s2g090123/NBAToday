package com.jiachian.nbatoday.rule

import androidx.test.core.app.ApplicationProvider
import com.jiachian.nbatoday.MainApplication
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class ApplicationRule : TestWatcher() {
    override fun starting(description: Description) {
        mockkObject(MainApplication)
        every { MainApplication.context } returns ApplicationProvider.getApplicationContext()
    }

    override fun finished(description: Description) {
        unmockkObject(MainApplication)
    }
}
