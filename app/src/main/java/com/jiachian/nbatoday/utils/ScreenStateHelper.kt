package com.jiachian.nbatoday.utils

import com.jiachian.nbatoday.compose.state.NbaScreenState
import com.jiachian.nbatoday.compose.state.NbaState
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.data.datastore.BaseDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class ScreenStateHelper(
    repository: BaseRepository,
    dataStore: BaseDataStore,
    state: NbaState? = null
) {
    private val viewModelProvider = ComposeViewModelProvider(
        repository = repository,
        dataStore = dataStore,
        screenStateHelper = this
    )
    private val initState = state ?: NbaState.Home(viewModelProvider.getHomeViewModel())
    private val stateStackImp = MutableStateFlow(listOf(initState))
    val stateStack = stateStackImp.asStateFlow()
    val currentState = stateStack.map { it.lastOrNull() ?: initState }

    fun openScreen(screenState: NbaScreenState) {
        val state = when (screenState) {
            NbaScreenState.Home -> {
                NbaState.Home(viewModelProvider.getHomeViewModel())
            }
            is NbaScreenState.BoxScore -> {
                NbaState.BoxScore(viewModelProvider.getBoxScoreViewModel(screenState.game))
            }
            is NbaScreenState.Team -> {
                NbaState.Team(viewModelProvider.getTeamViewModel(screenState.team))
            }
            is NbaScreenState.Player -> {
                NbaState.Player(viewModelProvider.getPlayerViewModel(screenState.playerId))
            }
            is NbaScreenState.Calendar -> {
                NbaState.Calendar(viewModelProvider.getCalendarViewModel(screenState.date))
            }
            is NbaScreenState.Bet -> {
                NbaState.Bet(viewModelProvider.getBetViewModel(screenState.account))
            }
        }
        pushState(state)
    }

    fun exitScreen() {
        popState()
    }

    private fun pushState(state: NbaState) {
        val stack = stateStack.value.toMutableList().apply {
            add(state)
        }
        stateStackImp.value = stack
    }

    private fun popState() {
        val state = stateStack.value.lastOrNull()
        state?.composeViewModel?.dispose()
        stateStackImp.value = stateStack.value.dropLast(1)
    }
}
