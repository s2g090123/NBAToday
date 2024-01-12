package com.jiachian.nbatoday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.compose.theme.updateColors
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.repository.RepositoryProvider
import com.jiachian.nbatoday.utils.ComposeViewModelProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel(
    private val repositoryProvider: RepositoryProvider,
    private val dataStore: BaseDataStore,
    private val navigationController: NavigationController,
    val viewModelProvider: ComposeViewModelProvider,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : ViewModel() {

    val navigationEvent = navigationController.eventFlow

    init {
        loadData()
    }

    /**
     * Loads data asynchronously.
     * 1. update schedules
     * 2. update teams
     * 3. update theme colors
     * 4. log in the user (if available)
     *
     * Finally, navigate to the home screen after loading data
     */
    private fun loadData() {
        viewModelScope.launch(dispatcherProvider.io) {
            val updateScheduleDeferred = async {
                repositoryProvider.schedule.updateSchedule()
            }
            val updateTeamsDeferred = async {
                repositoryProvider.team.insertTeams()
            }
            val updateColorsDeferred = async {
                dataStore
                    .themeColors
                    .first()
                    .also { updateColors(it) }
            }
            val loginUserDeferred = async {
                dataStore
                    .user
                    .first()
                    ?.also { user ->
                        repositoryProvider.user.login(user.account, user.password)
                    }
            }
            awaitAll(
                updateScheduleDeferred,
                updateTeamsDeferred,
                updateColorsDeferred,
                loginUserDeferred
            )
            navigationController.navigateToHome()
        }
    }

    fun consumeNavigationEvent(event: NavigationController.Event?) {
        navigationController.onEventConsumed(event)
    }
}
