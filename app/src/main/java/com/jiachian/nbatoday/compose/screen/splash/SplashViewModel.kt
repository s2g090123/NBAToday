package com.jiachian.nbatoday.compose.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiachian.nbatoday.common.Resource
import com.jiachian.nbatoday.compose.screen.splash.state.MutableSplashState
import com.jiachian.nbatoday.compose.screen.splash.state.SplashState
import com.jiachian.nbatoday.compose.theme.updateColors
import com.jiachian.nbatoday.usecase.schedule.ScheduleUseCase
import com.jiachian.nbatoday.usecase.team.TeamUseCase
import com.jiachian.nbatoday.usecase.user.UserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class Test {

}

class SplashViewModel(
    private val scheduleUseCase: ScheduleUseCase,
    private val teamUseCase: TeamUseCase,
    private val userUseCase: UserUseCase,
) : ViewModel() {
    private val stateImp = MutableSplashState()
    val state: SplashState = stateImp

    private val scheduleLoaded = MutableStateFlow(false)
    private val teamLoaded = MutableStateFlow(false)
    private val themeLoaded = MutableStateFlow(false)
    private val userLoaded = MutableStateFlow(false)

    private val loaded = combine(
        scheduleLoaded,
        teamLoaded,
        themeLoaded,
        userLoaded
    ) { schedule, team, theme, user ->
        schedule && team && theme && user
    }

    init {
        loaded
            .filter { it }
            .onEach { stateImp.loaded = true }
            .launchIn(viewModelScope)
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
        viewModelScope.launch {
            launch { loadSchedule() }
            launch { loadTeams() }
            launch { loadTheme() }
            launch { loadUser() }
        }
    }

    private suspend fun loadSchedule() {
        scheduleUseCase
            .updateSchedule()
            .onEach {
                when (it) {
                    is Resource.Error -> stateImp.error = it.error.asSplashError()
                    is Resource.Loading -> Unit
                    is Resource.Success -> scheduleLoaded.emit(true)
                }
            }
            .collect()
    }

    private suspend fun loadTeams() {
        teamUseCase
            .addTeams()
            .onEach {
                when (it) {
                    is Resource.Error -> stateImp.error = it.error.asSplashError()
                    is Resource.Loading -> Unit
                    is Resource.Success -> teamLoaded.emit(true)
                }
            }
            .collect()
    }

    private suspend fun loadTheme() {
        userUseCase
            .getTheme()
            .firstOrNull()
            ?.also {
                updateColors(it)
            }
        themeLoaded.emit(true)
    }

    private suspend fun loadUser() {
        userUseCase
            .getUser()
            .firstOrNull()
            ?.also { user ->
                userUseCase.userLogin(user.account, user.password)
            }
        userLoaded.emit(true)
    }
}
