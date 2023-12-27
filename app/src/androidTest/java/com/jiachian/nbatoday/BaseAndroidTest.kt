package com.jiachian.nbatoday

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.repository.RepositoryProvider
import com.jiachian.nbatoday.rule.CalendarRule
import com.jiachian.nbatoday.rule.CoroutineRule
import com.jiachian.nbatoday.rule.NBATeamRule
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
import org.koin.java.KoinJavaComponent.get

@OptIn(ExperimentalCoroutinesApi::class)
open class BaseAndroidTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val coroutineRule = CoroutineRule()

    @get:Rule
    val calendarRule = CalendarRule()

    @get:Rule
    val nbaTeamRule = NBATeamRule()

    protected val context: Context
        get() = ApplicationProvider.getApplicationContext()

    protected val dispatcherProvider: DispatcherProvider
        get() = coroutineRule.dispatcherProvider

    protected val dataHolder: DataHolder
        get() = get(DataHolder::class.java)

    protected val navigationController: NavigationController
        get() = get(NavigationController::class.java)

    protected val dataStore: BaseDataStore
        get() = get(BaseDataStore::class.java)

    protected val repositoryProvider: RepositoryProvider
        get() = get(RepositoryProvider::class.java)

    protected val composeViewModelProvider: ComposeViewModelProvider
        get() = get(ComposeViewModelProvider::class.java)

    protected fun launch(testBody: suspend TestScope.() -> Unit) = coroutineRule.launch {
        testBody()
    }

    protected fun <T> Flow<T>.launchAndCollect(): Job {
        return CoroutineScope(dispatcherProvider.unconfined).launch {
            collect()
        }
    }

    protected fun <T> Flow<T>.stateIn(initValue: T): StateFlow<T> {
        return stateIn(
            CoroutineScope(dispatcherProvider.unconfined),
            SharingStarted.Eagerly,
            initValue
        )
    }
}
