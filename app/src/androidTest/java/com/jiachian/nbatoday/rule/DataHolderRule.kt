package com.jiachian.nbatoday.rule

import com.jiachian.nbatoday.DataHolder
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class DataHolderRule : TestWatcher() {
    lateinit var dataHolder: DataHolder

    override fun starting(description: Description) {
        super.starting(description)
        dataHolder = DataHolder()
    }
}
