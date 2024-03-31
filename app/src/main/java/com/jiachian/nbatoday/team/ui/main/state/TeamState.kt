package com.jiachian.nbatoday.team.ui.main.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jiachian.nbatoday.team.ui.game.state.MutableTeamGamesState
import com.jiachian.nbatoday.team.ui.game.state.TeamGamesState
import com.jiachian.nbatoday.team.ui.main.event.TeamDataEvent
import com.jiachian.nbatoday.team.ui.player.state.MutableTeamPlayersState
import com.jiachian.nbatoday.team.ui.player.state.TeamPlayersState

@Stable
interface TeamState {
    val info: TeamInfoState
    val players: TeamPlayersState
    val games: TeamGamesState
    val loading: Boolean
    val notFound: Boolean
    val event: TeamDataEvent?
}

class MutableTeamState : TeamState {
    override val info: MutableTeamInfoState = MutableTeamInfoState()
    override val players: MutableTeamPlayersState = MutableTeamPlayersState()
    override val games: MutableTeamGamesState = MutableTeamGamesState()
    override var loading: Boolean by mutableStateOf(false)
    override var notFound: Boolean by mutableStateOf(false)
    override var event: TeamDataEvent? by mutableStateOf(null)
}
