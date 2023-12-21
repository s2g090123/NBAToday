package com.jiachian.nbatoday

import com.jiachian.nbatoday.coroutine.CoroutineEnvironment
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.koin.testModule
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.repository.RepositoryProvider
import com.jiachian.nbatoday.rule.CalendarRule
import com.jiachian.nbatoday.rule.CoroutineRule
import com.jiachian.nbatoday.utils.ComposeViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.Rule
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
open class BaseUnitTest : KoinTest {
    @get:Rule
    val coroutineRule = CoroutineRule()

    @get:Rule
    val calendarRule = CalendarRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(testModule)
    }

    protected val coroutineEnvironment: CoroutineEnvironment
        get() = coroutineRule.coroutineEnvironment

    protected val dispatcherProvider: DispatcherProvider
        get() = coroutineRule.dispatcherProvider

    protected val dataHolder: DataHolder
        get() = get()

    protected val navigationController: NavigationController
        get() = get()

    protected val dataStore: BaseDataStore
        get() = get()

    protected val repositoryProvider: RepositoryProvider
        get() = get()

    protected val composeViewModelProvider: ComposeViewModelProvider
        get() = get()

    protected fun launch(testBody: suspend TestScope.() -> Unit) = coroutineRule.launch {
        testBody()
    }

    protected fun <T> Flow<T>.launchAndCollect(): Job {
        return CoroutineScope(dispatcherProvider.unconfined).launch {
            collect()
        }
    }

    protected fun <T> Flow<T>.stateIn(initValue: T?): StateFlow<T?> {
        return stateIn(
            CoroutineScope(dispatcherProvider.unconfined),
            SharingStarted.Eagerly,
            initValue
        )
    }
}
