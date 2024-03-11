package com.jiachian.nbatoday

import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.koin.testModule
import com.jiachian.nbatoday.repository.RepositoryProvider
import com.jiachian.nbatoday.rule.CalendarRule
import com.jiachian.nbatoday.rule.CoroutineRule
import com.jiachian.nbatoday.rule.NBATeamRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
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
    val nbaTeamRule = NBATeamRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(testModule)
    }

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

    protected fun <T> SharedFlow<T>.defer(scope: CoroutineScope): Deferred<T> = scope.async {
        this@defer.first()
    }

    protected fun <T> Flow<T>.stateIn(initValue: T): StateFlow<T> {
        return stateIn(
            CoroutineScope(dispatcherProvider.unconfined),
            SharingStarted.Eagerly,
            initValue
        )
    }
}
