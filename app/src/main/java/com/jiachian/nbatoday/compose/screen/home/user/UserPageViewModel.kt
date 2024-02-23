package com.jiachian.nbatoday.compose.screen.home.user

import com.jiachian.nbatoday.compose.screen.state.UIState
import com.jiachian.nbatoday.compose.theme.updateColors
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.dispatcher.DefaultDispatcherProvider
import com.jiachian.nbatoday.dispatcher.DispatcherProvider
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.data.teamOfficial
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for handling business logic related to [UserPage].
 *
 * @property repository The repository for interacting with [User].
 * @property dataStore The data store for managing user preferences.
 * @property navigationController The controller for navigation within the app.
 * @property dispatcherProvider The provider for obtaining dispatchers for coroutines (default is [DefaultDispatcherProvider]).
 * @property coroutineScope The coroutine scope for managing coroutines (default is [CoroutineScope] with main dispatcher).
 */
class UserPageViewModel(
    private val repository: UserRepository,
    private val dataStore: BaseDataStore,
    private val navigationController: NavigationController,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.main)
) {
    // logged-in user
    private val user = repository.user

    // the UIState of user
    val userState = user.map { user ->
        UIState.Loaded(user)
    }.stateIn(coroutineScope, SharingStarted.Lazily, UIState.Loading())

    private val account = user.map {
        it?.account
    }.stateIn(coroutineScope, SharingStarted.Eagerly, null)

    // list of available NBA teams for theming
    val teams: List<NBATeam> = mutableListOf<NBATeam>().apply {
        add(teamOfficial)
        addAll(NBATeam.nbaTeams)
    }

    /**
     * Handles click event for navigating to the bet page.
     */
    fun onBetClick() {
        val account = account.value ?: return
        navigationController.navigateToBet(account)
    }

    /**
     * Updates the app theme based on the selected NBA team's colors.
     *
     * @param team The selected NBA team.
     */
    fun updateTheme(team: NBATeam) {
        coroutineScope.launch(dispatcherProvider.io) {
            updateColors(team.colors)
            dataStore.updateThemeColorsTeamId(team.teamId)
        }
    }

    fun login(account: String, password: String) {
        coroutineScope.launch(dispatcherProvider.io) {
            repository.login(account, password)
        }
    }

    fun logout() {
        coroutineScope.launch(dispatcherProvider.io) {
            repository.logout()
        }
    }

    fun register(account: String, password: String) {
        coroutineScope.launch(dispatcherProvider.io) {
            repository.register(account, password)
        }
    }
}
