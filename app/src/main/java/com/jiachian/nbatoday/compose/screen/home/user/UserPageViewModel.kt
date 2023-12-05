package com.jiachian.nbatoday.compose.screen.home.user

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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserPageViewModel(
    private val repository: UserRepository,
    private val dataStore: BaseDataStore,
    private val navigationController: NavigationController,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val coroutineScope: CoroutineScope = CoroutineScope(dispatcherProvider.unconfined)
) {

    val user = repository.user
        .stateIn(coroutineScope, SharingStarted.WhileSubscribed(5000), null)

    val nbaTeams: List<NBATeam> = mutableListOf<NBATeam>().apply {
        add(teamOfficial)
        addAll(NBATeam.nbaTeams)
    }

    fun openBetScreen() {
        val account = user.value?.account ?: return
        navigationController.navigateToBet(account)
    }

    fun updateTheme(team: NBATeam) {
        updateColors(team.colors)
        coroutineScope.launch(dispatcherProvider.io) {
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
