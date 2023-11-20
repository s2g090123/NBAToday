package com.jiachian.nbatoday

import android.app.Application
import android.content.Context
import com.jiachian.nbatoday.koin.module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    companion object {
        private var instance: Application? = null
        val context: Context
            get() = instance!!.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidContext(this@MainApplication)
            modules(module)
        }
    }
}
