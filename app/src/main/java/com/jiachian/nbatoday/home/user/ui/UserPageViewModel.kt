package com.jiachian.nbatoday.home.user.ui

import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.common.ui.theme.updateColors
import com.jiachian.nbatoday.home.user.domain.UserUseCase
import com.jiachian.nbatoday.home.user.ui.event.UserUIEvent
import com.jiachian.nbatoday.home.user.ui.state.MutableUserState
import com.jiachian.nbatoday.home.user.ui.state.UserState
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import com.jiachian.nbatoday.team.data.model.local.teams.teamOfficial
import kotlinx.coroutines.launch

class UserPageViewModel(
    private val userUseCase: UserUseCase,
) : ViewModel() {
    private val stateImp = MutableUserState()
    val state: UserState = stateImp

    init {
        collectUserChanged()
        collectThemeChanged()
    }

    private fun collectUserChanged() {
        stateImp.loading = true
        viewModelScope.launch {
            val teams = mutableListOf<NBATeam>().apply {
                add(teamOfficial)
                addAll(NBATeam.nbaTeams)
            }
            userUseCase
                .getUser()
                .collect { user ->
                    Snapshot.withMutableSnapshot {
                        stateImp.let { state ->
                            state.user = user
                            state.teams = teams
                            state.login = user != null
                            state.loading = false
                        }
                    }
                }
        }
    }

    private fun collectThemeChanged() {
        viewModelScope.launch {
            userUseCase
                .getTheme()
                .collect {
                    updateColors(it)
                }
        }
    }

    fun onEvent(event: UserUIEvent) {
        when (event) {
            UserUIEvent.Logout -> logout()
            is UserUIEvent.UpdateTheme -> updateTheme(event.teamId)
        }
    }

    private fun updateTheme(teamId: Int) {
        viewModelScope.launch {
            userUseCase.updateTheme(teamId)
        }
    }

    private fun logout() {
        viewModelScope.launch {
            userUseCase.userLogout()
        }
    }
}
