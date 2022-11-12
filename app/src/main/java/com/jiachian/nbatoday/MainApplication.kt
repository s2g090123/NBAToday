package com.jiachian.nbatoday

import android.app.Application
import com.jiachian.nbatoday.koin.module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(module)
        }
    }
}