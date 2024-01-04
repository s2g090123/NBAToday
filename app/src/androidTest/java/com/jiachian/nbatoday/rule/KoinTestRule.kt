package com.jiachian.nbatoday.rule

import androidx.test.core.app.ApplicationProvider
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

class KoinTestRule(
    private val module: Module
) : TestWatcher() {
    override fun starting(description: Description) {
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(module)
        }
    }

    override fun finished(description: Description) {
        stopKoin()
    }
}
