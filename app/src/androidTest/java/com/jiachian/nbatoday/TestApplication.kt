package com.jiachian.nbatoday

import android.app.Application
import com.jiachian.nbatoday.koin.testModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TestApplication)
            modules(testModule)
        }
    }
}
